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
	private Direction facingDirection = Direction.NORTH;
	//This is to indicate whether the walker has been caliberated in the beginning.
	private boolean caliberated = false;

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

		if(!caliberated){
			return caliberate(v);
		}
		return followLeftWall(v);
	}

	/**
	 * Caliberate the facing direction and position of the walker at the beginning point.
	 * The walker is caliberated once it finds a left wall.
	 * @param v
	 * @return
	 */
	private Direction caliberate(View v){
		//Step north if there are no adjoining walls
		if(!hasAdjacentWalls(v)){
			return Direction.NORTH;
		}
		else{
			//Otherwise,  the walker turns clockwise until it has a wall to its left
			while(!hasLeftWall(v)){
				turnClockwise();
				System.out.println(facingDirection+"=============");//debug
			}
			caliberated = true;
			//if blocked, keep turning clockwise
			while(isBlocked(v)){
				turnClockwise();
			}
			return facingDirection;
		}
	}

	/**
	 * This checks whether there is a wall in the facing direction blocking the movement.
	 * @param v
	 * @return
	 */
	private boolean isBlocked(View v){
		return !v.mayMove(facingDirection);
	}

	/**
	 * This uses the moving strategy of following the left wall. It gives the moving direction after caliberation.
	 * @param v
	 * @return
	 */
	private Direction followLeftWall(View v) {
		//try to find the left wall again at the turning wall corner
		if(!hasAdjacentWalls(v)){
			turnLeft(v);
		}
		//if blocked, see if there is a way to the left. If there is, turn left. Otherwise, turn clockwise.
		while(isBlocked(v)){
			if(!hasLeftWall(v)){
				turnLeft(v);
				break;
			}
			else{
				turnClockwise();
			}
		}
		return facingDirection;
	}

	/**
	 * This makes the walker turn left by changing its facingDirection.
	 * @param v
	 */
	private void turnLeft(View v){
		switch(this.facingDirection){
		case NORTH:
			facingDirection = Direction.WEST;
			break;
		case SOUTH:
			facingDirection = Direction.EAST;
			break;
		case WEST:
			facingDirection = Direction.SOUTH;
			break;
		case EAST:
			facingDirection = Direction.NORTH;
		}
	}

	/**
	 * Check if there is an adjacent wall around the walker at the start position.
	 * @param v
	 * @return
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

	/**
	 * This turns the facing direction clockwise. For example, Direction.NORTH will be turned to Direction.EAST.
	 */
	private void turnClockwise(){
		switch(this.facingDirection){
		case NORTH:
			facingDirection = Direction.EAST;
			break;
		case SOUTH:
			facingDirection = Direction.WEST;
			break;
		case WEST:
			facingDirection = Direction.NORTH;
			break;
		case EAST:
			facingDirection = Direction.SOUTH;
		}
	}

	/**
	 *  This checks if there is a wall to the left of the walker according to its facing direction.
	 * @param v
	 * @return
	 */
	private boolean hasLeftWall(View v){
		switch(this.facingDirection){
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