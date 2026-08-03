# Formal Mathematical Proof: RSA Decryption Correctness

## 1. Theorem Statement

Let $p$ and $q$ be two distinct prime numbers, $n = p \cdot q$, and $\phi(n) = (p - 1)(q - 1)$ be Euler's Totient function.
Let $e$ and $d$ be integer exponents satisfying $e \cdot d \equiv 1 \pmod{\phi(n)}$.

For any message integer $m \in \mathbb{Z}_n$, the decryption of the ciphertext $c \equiv m^e \pmod n$ yields the original message:
$$c^d \equiv (m^e)^d \equiv m^{e \cdot d} \equiv m \pmod n$$

---

## 2. Proof

### Step 1: Modulo $\phi(n)$ Relation

By definition of modular multiplicative inverse:
$$e \cdot d \equiv 1 \pmod{\phi(n)} \implies e \cdot d = k \cdot \phi(n) + 1 \quad \text{for some integer } k \ge 1$$

Thus, we need to show:
$$m^{e \cdot d} = m^{k \cdot \phi(n) + 1} \equiv m \pmod n$$

### Step 2: Case Analysis via Prime Factors

Since $n = p \cdot q$ where $p$ and $q$ are distinct primes, showing $m^{e \cdot d} \equiv m \pmod n$ is equivalent to showing:

1. $m^{e \cdot d} \equiv m \pmod p$
2. $m^{e \cdot d} \equiv m \pmod q$

#### Case A: $p \nmid m$ (Fermat's Little Theorem)

If $p$ does not divide $m$, then by Fermat's Little Theorem ($m^{p-1} \equiv 1 \pmod p$):
$$m^{e \cdot d} = m^{k(p-1)(q-1) + 1} = \left(m^{p-1}\right)^{k(q-1)} \cdot m \equiv (1)^{k(q-1)} \cdot m \equiv m \pmod p$$

#### Case B: $p \mid m$

If $p$ divides $m$, then $m \equiv 0 \pmod p$.
Thus:
$$m^{e \cdot d} \equiv 0^{e \cdot d} \equiv 0 \equiv m \pmod p$$

Since the congruence holds in both cases, $m^{e \cdot d} \equiv m \pmod p$ for all $m$.

Symmetrically, by the exact same logic for $q$:
$$m^{e \cdot d} \equiv m \pmod q$$

### Step 3: Chinese Remainder Theorem (CRT)

Since $p$ and $q$ are distinct prime numbers, $\gcd(p, q) = 1$.
Combining $m^{e \cdot d} \equiv m \pmod p$ and $m^{e \cdot d} \equiv m \pmod q$, by the Chinese Remainder Theorem:
$$m^{e \cdot d} \equiv m \pmod{p \cdot q} \implies m^{e \cdot d} \equiv m \pmod n$$

This completes the formal proof. $\blacksquare$
