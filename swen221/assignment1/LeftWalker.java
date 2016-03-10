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
	//This list has all the squares visited by the walker
	private List<Square> visited = new ArrayList<Square>();
	//This records the current square of the walker
	private Square current;

	public LeftWalker() {
		super("Left Walker");
		current = new Square(0,0);//let (0,0) be the starting square of the walker.
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
	 * This memorizes all the squares the walker has visited. It also updates the current square.
	 * @param d
	 */
	private void memorization(Direction d){
		current.setExit(d);
		visited.add(current);
		switch(d){
		case NORTH:
			current = new Square(current.x,current.y+1);
			break;
		case SOUTH:
			current = new Square(current.x,current.y-1);
			break;
		case WEST:
			current = new Square(current.x-1,current.y);
			break;
		case EAST:
			current = new Square(current.x+1,current.y);
		}
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
			memorization(Direction.NORTH);
			return Direction.NORTH;
		}
		else{
			//Otherwise,  the walker turns clockwise until it has a wall to its left
			while(!hasLeftWall(v)){
				turnClockwise();
			}
			caliberated = true;
			//if blocked, keep turning clockwise
			while(isBlocked(v)){
				turnClockwise();
			}
			memorization(facingDirection);
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
		if(!hasLeftWall(v)){
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
		//if the square has been visited before and the facing direction was the exit direction,
		//try a different exit direction.
		Square toBeRemoved = null;
		for(Square s: visited){
			if(s.equals(current) && (facingDirection == s.getExit())){
				toBeRemoved = s;
				turnClockwise();
				caliberated = false; //now we should recaliberate after turning to a new direction.
			}
		}
		visited.remove(toBeRemoved);
		memorization(facingDirection);
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

	/**
	 * A Square object records the position of the walker in relation to its starting point.
	 * The last attempted exit direction for the position is also recorded.
	 * @author yanlong
	 *
	 */
	private class Square{
		public final int x;// the horizontal position of the square
		public final int y;// the vertical position of the square
		private Direction exit;// the direction in which the walker exited the square last time
		/**
		 * The constructor for a Square object. It takes x and y arguments to finalize the position of the Square.
		 * @param x
		 * @param y
		 */
		public Square(int x, int y){
			this.x = x;
			this.y = y;
		}

		/**
		 * A setter for the exit direction.
		 * @param d
		 */
		public void setExit(Direction d){
			this.exit = d;
		}

		/**
		 * A getter for the exit direction.
		 * @return
		 */
		public Direction getExit(){
			return exit;
		}

		/**
		 * This checks the equality of two Square objects. They are equal if they have the same x and y values.
		 * @param other
		 * @return true if two squares are at the same position
		 */
		public boolean equals(Square other){
			return this.x==other.x && this.y==other.y;
		}
	}
}