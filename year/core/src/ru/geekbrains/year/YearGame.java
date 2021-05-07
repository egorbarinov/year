package ru.geekbrains.year;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class YearGame extends ApplicationAdapter {
    class House {
        private Vector2 position;
        private float colorFading;
        private float angle;

        public House(Vector2 position, float colorFading) {
            this.position = position;
            this.colorFading = colorFading;
            this.angle = MathUtils.random(-10.0f, 10.0f);
        }
    }

    public static final int WIDTH = 640;
    public static final int HEIGHT = 800;
    private SpriteBatch batch;
    private PolygonSpriteBatch polygonSpriteBatch;
    private Snow[] flakes;
    private Cloud[] clouds;
    private List<House>[] houses;
    private Moon moon;
    private Texture threadTexture;
    private Texture edgeTexture;
    private Texture houseTexture;
     private Music music;

    private PolygonSprite[] ground;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.polygonSpriteBatch = new PolygonSpriteBatch();
        this.threadTexture = new Texture("thread.png");
        this.edgeTexture = new Texture("edge.png");
        this.houseTexture = new Texture("house.png");
        this.clouds = new Cloud[10];
        this.houses = new ArrayList[4];
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        this.music.setLooping(true);
        this.music.play();
        for (int i = 0; i < clouds.length; i++) {
            clouds[i] = new Cloud();
        }
        this.flakes = new Snow[800];
        for (int i = 0; i < flakes.length; i++) {
            flakes[i] = new Snow(clouds);
        }

        this.moon = new Moon();
        this.ground = new PolygonSprite[]{
                generatePolygonSprite(0, 300, 140, 0.2f),
                generatePolygonSprite(1, 240, 80, 0.5f),
                generatePolygonSprite(2, 160, 60, 0.7f),
                generatePolygonSprite(3, 120, 120, 1.0f)
        };

    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        for (int i = 0; i < ground.length; i++) {
            polygonSpriteBatch.begin();
            ground[i].draw(polygonSpriteBatch);
            polygonSpriteBatch.end();
            batch.begin();
            for (int j = 0; j < houses[i].size(); j++) {
                House h = houses[i].get(j);
                batch.setColor(h.colorFading, h.colorFading, h.colorFading, 1);
                batch.draw(houseTexture, h.position.x - houseTexture.getWidth() / 2, h.position.y,
                        houseTexture.getWidth() / 2, 0, houseTexture.getWidth(), houseTexture.getHeight(),
                        h.colorFading / 3, h.colorFading / 3, h.angle, 0, 0, houseTexture.getWidth(),
                        houseTexture.getHeight(), false, false);
                batch.setColor(1, 1, 1, 1);
            }
            batch.end();
        }
        batch.begin();

        moon.render(batch, threadTexture);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < flakes.length; i++) {
            flakes[i].render(batch);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < clouds.length; i++) {
            clouds[i].render(batch, threadTexture);
        }

//        polygonSpriteBatch.begin();
//        ground[ground.length - 1].draw(polygonSpriteBatch);
//        polygonSpriteBatch.end();


        for (int i = 0; i < WIDTH / 40 + 1; i++) {
            batch.draw(edgeTexture, i * 40 - 20, -20);
            batch.draw(edgeTexture, i * 40 - 20, HEIGHT - 20);
        }
        for (int i = 0; i < HEIGHT / 40 + 1; i++) {
            batch.draw(edgeTexture, -20, i * 40 - 20);
            batch.draw(edgeTexture, WIDTH - 20, i * 40 - 20);
        }

        batch.end();
    }

    public void update(float dt) {
        for (int i = 0; i < flakes.length; i++) {
            flakes[i].update(dt);
        }
        for (int i = 0; i < clouds.length; i++) {
            clouds[i].update(dt);
        }
        moon.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void getCenter(float[] array, int left, int right, int rnd) {
        int mid = (left + right) / 2;
        array[mid] = (array[left] + array[right]) / 2 + MathUtils.random(-rnd, rnd);
        if (right - left > 1) {
            if (rnd > 2) {
                rnd /= 2;
            }
            getCenter(array, left, mid, rnd);
            getCenter(array, mid, right, rnd);
        }
    }

    private PolygonSprite generatePolygonSprite(int index, float baseHeight, int randomHeight, float colorFading) {
        int blockWidth = 5;
        int blockCount = WIDTH / blockWidth + 5;

        float[] vertices = new float[blockCount * 4];
        short[] indices = new short[blockCount * 6];

        float[] heightMap = new float[blockCount];
        heightMap[0] = baseHeight + MathUtils.random(-50, 50);
        heightMap[heightMap.length - 1] = baseHeight + MathUtils.random(-50, 50);
        getCenter(heightMap, 0, heightMap.length - 1, randomHeight);

        houses[index] = new ArrayList<>();
        int x = MathUtils.random(5, 10);
        do {
            houses[index].add(new House(new Vector2(x * blockWidth, heightMap[x] - 30), colorFading));
            x += MathUtils.random(20, 30);
        } while (x < heightMap.length);

        float maxHeight = 0.0f;
        for (int i = 0; i < heightMap.length; i++) {
            if (heightMap[i] > maxHeight) {
                maxHeight = heightMap[i];
            }
        }

        for (int i = 0; i < blockCount; i++) {
            vertices[i * 4 + 0] = i * blockWidth;
            vertices[i * 4 + 1] = 0;
            vertices[i * 4 + 2] = i * blockWidth;
            vertices[i * 4 + 3] = heightMap[i];
        }

        for (int i = 0; i < blockCount - 1; i++) {
            indices[i * 6 + 0] = (short) (i * 2 + 0);
            indices[i * 6 + 1] = (short) (i * 2 + 1);
            indices[i * 6 + 2] = (short) (i * 2 + 2);
            indices[i * 6 + 3] = (short) (i * 2 + 1);
            indices[i * 6 + 4] = (short) (i * 2 + 3);
            indices[i * 6 + 5] = (short) (i * 2 + 2);
        }

        Pixmap pix = new Pixmap(2, (int) maxHeight, Pixmap.Format.RGBA8888);
        for (int i = 0; i < maxHeight; i++) {
            float c = (1.0f - i / maxHeight) * colorFading;
            pix.setColor(c, c, c, 1.0f);
            pix.fillRectangle(0, i * 4, 2, 4);
        }
        TextureRegion textureRegion = new TextureRegion(new Texture(pix));

        return new PolygonSprite(new PolygonRegion(textureRegion, vertices, indices));
    }
}
