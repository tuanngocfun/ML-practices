## Understanding Noise in Stochastic Gradient Descent (SGD)

### 1. Gradient Computation in Different Methods

Let’s define:
- $\mathcal{L}(\theta)$ as the loss function.
- $\theta$ as the model parameters (weights).
- A dataset with $N$ training samples: $\{x_1, x_2, ..., x_N\}$.

#### **Batch Gradient Descent (BGD)**

Batch gradient descent computes the gradient over the **entire dataset** before updating weights:

```math
\theta^{(t+1)} = \theta^{(t)} - \eta \cdot \frac{1}{N} \sum_{i=1}^{N} \nabla \mathcal{L}(x_i, \theta)
```

- The gradient is **averaged** over all samples, making updates **stable**.
- However, this requires **large memory** and is computationally expensive.

#### **Stochastic Gradient Descent (SGD)**

SGD updates weights after processing each individual sample:

```math
\theta^{(t+1)} = \theta^{(t)} - \eta \cdot \nabla \mathcal{L}(x_i, \theta)
```

- Each sample provides a different gradient estimate.
- Updates are **noisy** due to **high variance**.
- Allows for **online learning** but can lead to **instability** in convergence.

#### **Mini-Batch Gradient Descent**

Mini-batch gradient descent computes the gradient over a small subset of $m$ samples:

```math
\theta^{(t+1)} = \theta^{(t)} - \eta \cdot \frac{1}{m} \sum_{j=1}^{m} \nabla \mathcal{L}(x_j, \theta)
```

- Balances between **stability** and **computational efficiency**.
- Reduces **variance** while maintaining the ability to generalize well.

---

### 2. Variance Reduction in Gradient Updates

The **variance of the mean of independent samples** follows:

```math
\text{Var}(\bar{X}) = \frac{1}{m} \text{Var}(X)
```

where:
- $X$ is the gradient from one sample.
- $\bar{X}$ is the mean of $m$ samples.

From this, we get:

```math
\text{Var}(\nabla \mathcal{L}_{\text{batch}}) = \frac{1}{m} \text{Var}(\nabla \mathcal{L}_{\text{SGD}})
```

This means:
- **Batch gradient descent ($m = N$)** has **zero variance**.
- **SGD ($m = 1$)** has the **highest variance**.
- **Mini-batch ($1 < m < N$)** provides a **trade-off**.

---

### 3. Optimization Path Behavior
- **Batch Gradient Descent:** Smooth trajectory, converging directly to the minimum.
- **SGD:** High fluctuations due to noise, but can escape local minima.
- **Mini-Batch Gradient Descent:** Less noise, more stable convergence than SGD.

---

### 4. Conclusion
- **Averaging gradients reduces variance**, making updates smoother.
- **SGD is noisy** because each update is based on a single sample.
- **Mini-batch gradient descent balances efficiency and stability**.

This mathematical reasoning explains why **not averaging errors leads to a noisier process** in SGD.
