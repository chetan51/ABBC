package main.java.RSAEngine;

import java.math.BigInteger;
import java.util.*;

public class KeyGen {
	
	/*	Finds the GCD of the two numbers using the Extended Euclidean algorithm.
	 */
	public BigInteger[] ExtendedGCD(BigInteger m, BigInteger e) {
		if(e.equals(BigInteger.ZERO)) {								// if e == 0
			BigInteger[] ret = {BigInteger.ONE, BigInteger.ZERO};	// return {0, 1}
			return ret;
		}
		else {
			BigInteger[] arr = ExtendedGCD(e, m.mod(e));			// arr = {a, b} = egcd(e, m % e)
			return new BigInteger[] {arr[1], 						
				arr[0].subtract((m.divide(e)).multiply(arr[1]))};	// return {b, a - (m / e) * b }
		}
	}
	
	/*	Finds an integer co-prime to the one given.
	 */
	public BigInteger FindCoprime(BigInteger x){
		BigInteger count = BigInteger.valueOf(2);				// count = 2
		while(!count.gcd(x).equals(BigInteger.ONE)) {			// while gcd(x, count) != 1
			count = count.add(BigInteger.ONE);					// count++;
		}
		return count;											// returns count coprime to x
	}
	
	/*	Generates an RSA key of the given length.
	 */
	public BigInteger[] GenerateKey(int length) {
		BigInteger p, q, m, e, d;
		Random rand = new Random();	
		p = BigInteger.probablePrime(length, rand); 			// Generate prime p
		q = BigInteger.probablePrime(length, rand); 			// Generate prime q
		BigInteger j = p.subtract(BigInteger.ONE);				// j = p - 1
		BigInteger k = q.subtract(BigInteger.ONE);				// k = q - 1
		BigInteger n = p.multiply(q);							// n = p * q
		m = j.multiply(k); 										// m = (p-1)(q-1)
		e = FindCoprime(m);										// find e coprime to m
		BigInteger[] temp = ExtendedGCD(m, e);					// use Extended-GCD
		d = temp[1];											// d = inverse of e mod (p-1)(q-1)
		if(d.signum() == -1)									// if d is negative, add the mod to it
			d = d.add(m);
		BigInteger[] key = {e, d, n};
		return key;												// key = {e, d, n}
	}
	
}