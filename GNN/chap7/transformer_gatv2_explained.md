## Multi-head Attention

Multi-head attention is a technique introduced by Vaswani et al. (2017) in the original Transformer paper. Instead of computing a single attention score, it calculates multiple embeddings with their own attention scores. This enhances the model's ability to capture diverse features from input data.

### **Combining Multi-Head Attention Results**
After computing attention scores for multiple heads, we can aggregate them using two methods:

1. **Averaging (Mean Pooling):**
   - The embeddings from different attention heads are summed and normalized by the number of heads.
   - Formula:
```math
h_i = \frac{1}{n} \sum_{k=1}^{n} h_i^k = \frac{1}{n} \sum_{k=1}^{n} \sum_{j \in N_i} \alpha_{ij}^k W^k x_j
```
     where:
     - $h_i$ is the final aggregated embedding for token $i$.
     - $n$ is the number of attention heads.
     - $\alpha_{ij}^k$ represents the attention coefficient for head $k$ between tokens $i$ and $j$.
     - $W^k$ is the learned weight matrix for head $k$.
     - $x_j$ is the embedding of token $j$.
   - Used in the **final layer** of the network.

2. **Concatenation:**
   - Instead of averaging, embeddings are concatenated to form a larger vector.
   - Formula:
```math
h_i = \|_{k=1}^{n} h_i^k = \|_{k=1}^{n} \sum_{j \in N_i} \alpha_{ij}^k W^k x_j
```
     where:
     - The notation $\|_{k=1}^{n}$ indicates concatenation across all heads.
   - Used in **hidden layers** to retain more information.

#### **Practical Usage**
- **Concatenation** is preferred in hidden layers.
- **Averaging** is used in the final layer to summarize attention information.

---

## Improved Graph Attention Layer (GATv2)

Brody et al. (2021) improved the standard **Graph Attention Layer (GAT)** by introducing **GATv2**, which computes a more expressive **dynamic attention**.

### **Comparison between GAT and GATv2**

#### **Original Graph Attention Operator (GAT)**
```math
\alpha_{ij} = \frac{\exp \left( W_{att}^{t} \text{LeakyReLU}(W[x_i || x_j]) \right)}{\sum_{k \in N_i} \exp \left( W_{att}^{t} \text{LeakyReLU}(W[x_i || x_k]) \right)}
```
where:
- $\alpha_{ij}$ is the attention coefficient between nodes $i$ and $j$.
- $W_{att}^{t}$ is the attention weight matrix at time step $t$.
- $W$ is the learned weight matrix applied to node embeddings.
- $x_i$ and $x_j$ are the embeddings of nodes $i$ and $j$.
- $||$ represents concatenation.
- $N_i$ is the neighborhood of node $i$.

#### **Modified Operator (GATv2)**
```math
\alpha_{ij} = \frac{\exp \left( W_{att}^{t} \text{LeakyReLU}(W[x_i || x_j]) \right)}{\sum_{k \in N_i} \exp \left( W_{att}^{t} \text{LeakyReLU}(W[x_i || x_k]) \right)}
```
where:
- The key change is applying the weight matrix $W$ **after** the attention weight computation, making the model more expressive.

#### **Why Prefer GATv2?**
- **More dynamic and expressive** attention mechanism.
- **Outperforms GAT** consistently in experimental benchmarks.

Thus, **GATv2 should be used whenever possible.**

---

## Implementing Graph Attention Layer in NumPy

Since neural networks rely on matrix multiplications, we need to implement GAT/GATv2 using matrix operations.

### **Mathematical Representation**
1. **Graph Attention Layer formula:**
```math
h_i = \sum_{j \in N_i} \alpha_{ij} W x_j
```
   where:
   - $h_i$ is the updated node representation.
   - $\alpha_{ij}$ is the attention coefficient between nodes $i$ and $j$.
   - $W$ is the learned weight matrix.
   - $x_j$ is the embedding of node $j$.

2. **Matrix Formulation:**
```math
H = \tilde{A} W_{\alpha} X W^T
```
   where:
   - $H$ is the matrix of updated node representations.
   - $\tilde{A}$ is the normalized adjacency matrix of the graph.
   - $W_{\alpha}$ is the matrix storing all $\alpha_{ij}$ attention coefficients.
   - $X$ is the matrix of input node embeddings.
   - $W$ is the learned weight matrix.

By implementing these formulas efficiently, we can speed up multi-head attention computations.

---

## **Summary**
- **Multi-head attention** helps capture diverse relationships between input features.
- **Two aggregation methods:** Averaging for the final layer, concatenation for hidden layers.
- **GATv2 improves over GAT** by changing the order of weight applications, leading to better expressivity.
- **Matrix multiplication is key** for efficient GAT/GATv2 implementations.