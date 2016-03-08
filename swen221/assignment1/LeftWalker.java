package swen221.assignment1;

import java.util.ArrayList;
import java.util.List;

import maze.*;

/**
 * An implementation of the left walker, which you need to complete according to
 * the specification given in the assignment handout.
 *
 */
public class LeftWalker extends Walker {
	//This is the facing direction of the walker. The walker faces north at the start position.
	private Direction facing = Direction.NORTH;

	public LeftWalker() {
		super("Left Walker");
	}

	@Override
	protected Direction move(View v) {
		// TODO: you need to implement this method according to the
		// specification given for the left walker!!

		// Use the pause() command to slow the simulation down so you can see
		// what's happening...
		pause(100);

		// Currently, the left walker just follows a completely random
		// direction. This is not what the specification said it should do, so
		// tests will fail ... but you should find it helpful to look at how it
		// works!

		//Step north if there are no adjoining walls
		if(!hasAdjacentWalls(v)){
			System.out.println(facing);//debug
			return Direction.NORTH;
		}
		else{
			//Otherwise,  the walker turns clockwise until it has a wall to its left
			while(!hasLeftWall(v)){
				turnClockwise();
				System.out.println(facing+"=============");//debug
			}
			//move forward in the facing direction to follow the left wall
			return facing;
		}
	}

	/*
	 * Check if there is an adjacent wall around the walker at the start position.
	 */
	private boolean hasAdjacentWalls(View v){
		Direction[] allDirections = Direction.values();
		for (Direction d : allDirections) {
			if (!v.mayMove(d)) {
				//Yes, there is an adjacent wall
				return true;
			}
		}
		return false;
	}

	/*
	 * This turns the facing direction clockwise. For example, Direction.NORTH will be turned to Direction.EAST.
	 */
	private void turnClockwise(){
		switch(this.facing){
		case NORTH:
			facing = Direction.EAST;
			break;
		case SOUTH:
			facing = Direction.WEST;
			break;
		case WEST:
			facing = Direction.NORTH;
			break;
		case EAST:
			facing = Direction.SOUTH;
		}
	}

	/*
	 * This checks if there is a wall to the left of the walker according to its facing direction.
	 */
	private boolean hasLeftWall(View v){
		switch(this.facing){
		case NORTH:
			return !v.mayMove(Direction.WEST);
		case SOUTH:
			return !v.mayMove(Direction.EAST);
		case WEST:
			return !v.mayMove(Direction.SOUTH);
		case EAST:
			return !v.mayMove(Direction.NORTH);
		default: return false;
		}
	}

	/**
	 * This simply returns a randomly chosen (valid) direction which the walker
	 * can move in.
	 *
	 * @param View
	 *            v
	 * @return
	 */
	private Direction getRandomDirection(View v) {
		// The random walker first decides what directions it can move in. The
		// walker cannot move in a direction which is blocked by a wall.
		List<Direction> possibleDirections = determinePossibleDirections(v);

		// Second, the walker chooses a random direction from the list of
		// possible directions
		return selectRandomDirection(possibleDirections);
	}

	/**
	 * Determine the list of possible directions. That is, the directions which
	 * are not blocked by a wall.
	 *
	 * @param v
	 *            The View object, with which we can determine which directions
	 *            are possible.
	 * @return
	 */
	private List<Direction> determinePossibleDirections(View v) {
		Direction[] allDirections = Direction.values();
		ArrayList<Direction> possibleDirections = new ArrayList<Direction>();

		for (Direction d : allDirections) {
			if (v.mayMove(d)) {
				// Yes, this is a valid direction
				possibleDirections.add(d);
			}
		}

		return possibleDirections;
	}

	/**
	 * Select a random direction from a list of possible directions.
	 *
	 * @param possibleDirections
	 * @return
	 */
	private Direction selectRandomDirection(List<Direction> possibleDirections) {
		int random = (int) (Math.random() * possibleDirections.size());
		return possibleDirections.get(random);
	}
}