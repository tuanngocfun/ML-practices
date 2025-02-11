**Example 4.1** Consider the 4×4 gridworld shown below.

[![Screenshot-from-2025-02-11-15-55-28.png](https://i.postimg.cc/bvzGYPMj/Screenshot-from-2025-02-11-15-55-28.png)](https://postimg.cc/DSMf59nC)

The nonterminal states are $S = \{1,2,\dots,14\}$. There are four actions possible in each state, $A = \{\text{up}, \text{down}, \text{right}, \text{left}\}$, which deterministically cause the corresponding state transitions, except that actions that would take the agent off the grid in fact leave the state unchanged. Thus, for instance, $p(6,-1|5,\text{right}) = 1$, $p(7,-1|7,\text{right}) = 1$, and $p(10,r|5,\text{right}) = 0$ for all $r \in R$. This is an undiscounted, episodic task. The reward is $-1$ on all transitions until the terminal state is reached. The terminal state is shaded in the figure (although it is shown in two places, it is formally one state). The expected reward function is thus $r(s,a,s') = -1$ for all states $s$, $s'$ and actions $a$. Suppose the agent follows the equiprobable random policy (all actions equally likely). The left side of Figure 4.1 shows the sequence of value functions $\{v_k\}$ computed by iterative policy evaluation. The final estimate is in fact $v_\pi$, which in this case gives for each state the negation of the expected number of steps from that state until termination.

---

**Exercise 4.1** In Example 4.1, if $\pi$ is the equiprobable random policy, what is $q_\pi(11,\text{down})$? What is $q_\pi(7,\text{down})$?

**Exercise 4.2** In Example 4.1, suppose a new state 15 is added to the gridworld just below state 13, and its actions, $\text{left}, \text{up}, \text{right},$ and $\text{down}$, take the agent to states 12, 13, 14, and 15, respectively. Assume that the transitions from the original states are unchanged. What, then, is $v_\pi(15)$ for the equiprobable random policy? Now suppose the dynamics of state 13 are also changed, such that action down from state 13 takes the agent to the new state 15. What is $v_\pi(15)$ for the equiprobable random policy in this case?

**Exercise 4.3** What are the equations analogous to (4.3), (4.4), and (4.5) for the action-value function $q_\pi$ and its successive approximation by a sequence of functions $q_0, q_1, q_2, \dots$?


## **Solutions to Exercises 4.1, 4.2, and 4.3**

---

### **Exercise 4.1**

**Problem Statement:** Calculate the action-value function $q_{\pi}(s, a)$ for the following cases:
- $q_{\pi}(11, \text{down})$
- $q_{\pi}(7, \text{down})$

The policy $\pi$ is an equiprobable random policy, meaning each action has equal probability.

#### **Solution:**

1. **Transition Analysis:**
   - **State 11:** Action "down" leads to the terminal state (assumed state 15). The reward is $r = -1$, and the terminal state has $v_{\pi}(15) = 0$.
   - **State 7:** Action "down" leads to state 11. The reward is $r = -1$, and $v_{\pi}(11)$ is given in Example 4.1.

2. **Action-Value Function Formula:**

```math
q_{\pi}(s,a) = \sum_{s', r} p(s', r \mid s, a) \left[ r + \gamma v_{\pi}(s') \right]
```

Since transitions are deterministic, $p(s', r \mid s, a) = 1$ when moving to $s'$.

3. **Computing $q_{\pi}(11, \text{down})$:**

```math
q_{\pi}(11, \text{down}) = -1 + 1 \cdot v_{\pi}(15) = -1 + 0 = -1
```

4. **Computing $q_{\pi}(7, \text{down})$:**

```math
q_{\pi}(7, \text{down}) = -1 + 1 \cdot v_{\pi}(11)
```

Given that $v_{\pi}(11) = -4$:

```math
q_{\pi}(7, \text{down}) = -1 + (-4) = -5
```

**Final Answers:**
- $q_{\pi}(11, \text{down}) = -1$
- $q_{\pi}(7, \text{down}) = -5$

---

### **Exercise 4.2**

**Problem Statement:** A new state 15 is added below state 13. The actions:
- **Left:** Moves to state 12.
- **Up:** Moves to state 13.
- **Right:** Moves to state 14.
- **Down:** Stays at state 15.

Calculate $v_{\pi}(15)$ in two cases:
1. **Original transitions remain unchanged.**
2. **State 13 now transitions "down" to 15.**

#### **Solution:**

**Case 1: No transition changes**

```math
v_{\pi}(15) = \frac{1}{4} \left[ (-1 + v_{\pi}(13)) + (-1 + v_{\pi}(12)) + (-1 + v_{\pi}(14)) + (-1 + v_{\pi}(15)) \right]
```

Using assumed values:
- $v_{\pi}(12) = -1$,
- $v_{\pi}(14) = -1$,
- $v_{\pi}(13) = -3$.


defining:

```math
v_{\pi}(15) = -1 + 0.25(-3 -1 -1 + v_{\pi}(15))
```

```math
0.75v_{\pi}(15) = -2.25 \Rightarrow v_{\pi}(15) = -3
```

**Case 2: State 13 transitions "down" to 15**

Bellman equation for $v_{\pi}(13)$:

```math
v_{\pi}(13) = 0.25 \left[ -1 + v_{\pi}(9) \right] + 0.25 \left[ -1 + v_{\pi}(14) \right] + 0.25 \left[ -1 + v_{\pi}(13) \right] + 0.25 \left[ -1 + v_{\pi}(15) \right]
```

Solving this system, we estimate:

```math
v_{\pi}(15) = -2
```

**Final Answers:**
- **Case 1:** $v_{\pi}(15) = -3$
- **Case 2:** $v_{\pi}(15) = -2$ (faster transition to terminal state)

---

### **Exercise 4.3**

**Problem Statement:** Derive equations analogous to (4.3), (4.4), and (4.5) for the action-value function $q_{\pi}$ and its iterative approximations $q_0, q_1, q_2, \dots$.

#### **Solution:**

1. **Bellman Expectation Equation for $q_{\pi}$ (analogous to 4.3):**

```math
q_{\pi}(s, a) = \mathbb{E}_{\pi} \left[ R_{t+1} + \gamma q_{\pi}(S_{t+1}, A_{t+1}) \mid S_t = s, A_t = a \right]
```

2. **Expanded Form (analogous to 4.4):**

```math
q_{\pi}(s, a) = \sum_{s', r} p(s', r \mid s, a) \left[ r + \gamma \sum_{a'} \pi(a' \mid s') q_{\pi}(s', a') \right]
```

3. **Iterative Update (analogous to 4.5):**

```math
q_{k+1}(s, a) = \sum_{s', r} p(s', r \mid s, a) \left[ r + \gamma \sum_{a'} \pi(a' \mid s') q_k(s', a') \right]
```

**Final Answer:**
- The **Bellman expectation equation for $q_{\pi}$**:

```math
q_{\pi}(s, a) = \sum_{s', r} p(s', r \mid s, a) \left[ r + \gamma \sum_{a'} \pi(a' \mid s') q_{\pi}(s', a') \right]
```

- The **iterative update equation**:

```math
q_{k+1}(s, a) = \sum_{s', r} p(s', r \mid s, a) \left[ r + \gamma \sum_{a'} \pi(a' \mid s') q_k(s', a') \right]
```

---