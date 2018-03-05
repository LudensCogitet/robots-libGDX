package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class RobotsGame extends ApplicationAdapter {
	public enum InputTypes {None, Move, Teleport, Up, Down, Left, Right, UpLeft, UpRight, DownLeft, DownRight}

	private Vector2 wouldMove = null;

	private SpriteBatch batch;
	private Texture sheet;
	private TextureRegion[] sprites;

	private FitViewport viewport;
	public final float squareSize = 64;

	private int worldWidth;
	private int worldHeight;

	public Player player;

	private int robotInc = 5;
	private int numRobotsStart = 5;
	private int numRobots = numRobotsStart;
	private int numRobotsAlive;

	private Array<RobotsEntity> entities;
	private Array<RobotsEntity> entitiesToRemove;

	private Vector2 calculateNewPlayerPos(InputTypes direction) {
		int x = (int) player.pos.x;
		int y = (int) player.pos.y;

		switch(direction) {
			case Up:
				if(player.pos.y < worldHeight -1) {
					y = (int) player.pos.y + 1;
				}
				break;
			case UpRight:
				if(player.pos.y < worldHeight -1 && player.pos.x < worldWidth -1) {
					y = (int) player.pos.y + 1;
					x = (int) player.pos.x + 1;
				}
				break;
			case Right:
				if(player.pos.x < worldWidth -1) {
					x = (int) player.pos.x + 1;
				}
				break;
			case DownRight:
				if(player.pos.y > 0 && player.pos.x < worldWidth - 1) {
					y = (int) player.pos.y - 1;
					x = (int) player.pos.x + 1;
				}
				break;
			case Down:
				if(player.pos.y > 0) {
					y = (int) player.pos.y - 1;
				}
				break;
			case DownLeft:
				if(player.pos.y > 0 && player.pos.x > 0) {
					y = (int) player.pos.y - 1;
					x = (int) player.pos.x -1;
				}
				break;
			case Left:
				if(player.pos.x > 0) {
					x = (int) player.pos.x -1;
				}
				break;
			case UpLeft:
				if(player.pos.y < worldHeight -1 && player.pos.x > 0) {
					y = (int) player.pos.y + 1;
					x = (int) player.pos.x - 1;
				}
				break;
			default:
				return null;
		}

		return new Vector2(x,y);
	}

	public void kill(RobotsEntity entity, boolean remove) {
		if(remove && !entitiesToRemove.contains(entity, true)) {
			entitiesToRemove.add(entity);
		}

		numRobotsAlive--;
	}

	public int indexFromCoords(Vector2 coords) {
		return (int) (coords.x + (coords.y * worldWidth));
	}

	public Vector2 coordsFromIndex(int index) {
		int x = index % worldWidth;
		int y = (index - x) / worldWidth;
		return new Vector2(x, y);
	}

	public static TextureRegion[][] fixBleeding(TextureRegion[][] region) {
		for (TextureRegion[] array : region) {
			for (TextureRegion texture : array) {
				fixBleeding(texture);
			}
		}

		return region;
	}

	public static TextureRegion fixBleeding(TextureRegion region) {
		float fix = 0.01f;

		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims
		// region
		return region;
	}

	private void drawPlayerSpecter(SpriteBatch batch) {
		Color oldColor = batch.getColor();
		batch.setColor(1, 1, 1,  0.3f);
		batch.draw(sprites[player.type.sprite], (int) wouldMove.x, (int) wouldMove.y, 1, 1);
		batch.setColor(oldColor);
	}

	public Array<Integer> getShuffledRange(int size) {
		Array<Integer> range = new Array<Integer>(size);

		for(int i = 0; i < size; i++) {
			range.add(i);
		}

		range.shuffle();

		return range;
	}

	public void input(InputTypes type) {
		if(type == InputTypes.Move && wouldMove != null) {
			player.move(wouldMove);
		} else if(type == InputTypes.Teleport) {
			player.move(new Vector2(MathUtils.random(worldWidth-1), MathUtils.random(worldHeight-1)));
		}

		for(RobotsEntity i : entities) {
			i.act();
		}

		for(RobotsEntity i : entities) {
			for(int j = 0; j < entities.size; j++) {
				i.resolve(entities.get(j));
			}
		}

		cleanEntities();

		if(numRobotsAlive <= 0) {
			init(numRobots + robotInc);
		}
	}

	public void wouldInput(InputTypes type) {
		wouldMove = calculateNewPlayerPos(type);
	}

	private void  cleanEntities() {
		entities.removeAll(entitiesToRemove, true);
		entitiesToRemove.clear();
	}

	private void init(int newNumRobots) {
		numRobots = newNumRobots;

		Array<Integer> positions = getShuffledRange(worldWidth * worldHeight);

		Vector2 playerPos = coordsFromIndex(positions.pop());

		player = new Player(this, (int) playerPos.x, (int) playerPos.y);
		entities = new Array<RobotsEntity>(numRobots);
		entitiesToRemove = new Array<RobotsEntity>(numRobots);

		for(int i = 0; i < numRobots; i++) {
			Vector2 pos = coordsFromIndex(positions.pop());
			entities.add(new Robot(this, (int) pos.x, (int) pos.y));
		}

		numRobotsAlive = numRobots;
	}

	public void restart() {
		init(numRobotsStart);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new RobotsTouchListener(this));

		batch = new SpriteBatch();
		sheet = new Texture(Gdx.files.internal("spriteSheet.png"));

		sprites = fixBleeding(TextureRegion.split(sheet, 32, 32))[0];

		worldWidth = (int)(Gdx.graphics.getWidth() / squareSize);
		worldHeight = (int)(Gdx.graphics.getHeight() / squareSize);

		init(numRobots);

		viewport = new FitViewport(worldWidth, worldHeight);
		viewport.apply(true);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		batch.draw(sprites[player.type.sprite],
					(int) player.pos.x,
					(int) player.pos.y,
					1,1);

		if(wouldMove != null) {
			drawPlayerSpecter(batch);
		}

		for(RobotsEntity entity : entities) {
			batch.draw(sprites[entity.type.sprite], (int) entity.pos.x, (int) entity.pos.y, 1, 1);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sheet.dispose();
	}
}
