package com.tictim.railreborn.api;

/**
 * <b>R A I L C R A F T J O U L E</b><br>
 * no it is actually <b>R E B O R N J O U L E</b><br>
 * no! it is actually <b>R E F I N E D J O U L E</b><br>
 * k stap<br>
 * TODO change name
 */
public interface RJ{
	long capacityRJ();
	long currentRJ();
	long maxRJIn();
	long maxRJOut();
	
	/**
	 * @return amount of RJ that was accepted
	 */
	long insertRJ(long amount, boolean internally, boolean simulate);
	/**
	 * @return amount of RJ that was extracted
	 */
	long extractRJ(long amount, boolean internally, boolean simulate);
}
