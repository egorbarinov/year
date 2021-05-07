package ru.geekbrains.year;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static ru.geekbrains.year.YearGame.*;

public class Snow {
    private static Texture texture;
    private Cloud[] clouds;
    private Vector2 position;
    private Vector2 velocity;
    private float size;

    public Snow(Cloud[] clouds) {
        if (texture == null) {
            texture = new Texture("snow.png");
        }
        this.clouds = clouds;
        this.position = new Vector2(MathUtils.random(WIDTH), MathUtils.random(HEIGHT));
        this.velocity = new Vector2(MathUtils.random(-15.0f, 15.0f), MathUtils.random(-60.0f, -20.0f));
        this.size = MathUtils.random(6.0f, 12.0f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, size, size);
        if(MathUtils.random(1000) < 5) {
            batch.draw(texture, position.x, position.y, size * 1.4f, size * 1.4f);
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.y < -20) {
            int cloudIndex = MathUtils.random(clouds.length - 1);
            position.set(clouds[cloudIndex].getRandomXPoint(), clouds[cloudIndex].getYPoint());
            velocity.set(MathUtils.random(-15.0f, 15.0f), MathUtils.random(-60.0f, -20.0f));
            size = MathUtils.random(6.0f, 12.0f);
        }
    }
}
