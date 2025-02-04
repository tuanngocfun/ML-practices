# **Detailed Analysis of Graph Embedding Methods**

## 1. Local Heuristics
Local heuristics use neighborhood information of nodes to measure their similarity in the graph.

### **a. Common Neighbors**
```math
f(u, v) = |\mathcal{N}(u) \cap \mathcal{N}(v)|
```
**Notation:**
- $u, v$ – Two nodes in the graph.
- $\mathcal{N}(u)$ – Set of neighbors of $u$.
- $\mathcal{N}(v)$ – Set of neighbors of $v$.
- $\mathcal{N}(u) \cap \mathcal{N}(v)$ – Intersection of the two neighbor sets.
- $| \cdot |$ – Cardinality of the set.

### **b. Jaccard’s Coefficient**
```math
f(u, v) = \frac{|\mathcal{N}(u) \cap \mathcal{N}(v)|}{|\mathcal{N}(u) \cup \mathcal{N}(v)|}
```
**Notation:**
- Same as Common Neighbors.
- $\mathcal{N}(u) \cup \mathcal{N}(v)$ – Union of the two neighbor sets.

### **c. Adamic-Adar Index**
```math
f(u, v) = \sum_{x \in \mathcal{N}(u) \cap \mathcal{N}(v)} \frac{1}{\log |\mathcal{N}(x)|}
```
**Notation:**
- $x$ – A common neighbor of $u$ and $v$.
- $|\mathcal{N}(x)|$ – Number of neighbors of node $x$.
- $\log |\mathcal{N}(x)|$ – Logarithm of the number of neighbors of $x$.
- $\frac{1}{\log |\mathcal{N}(x)|}$ – Inverse logarithm value.

---

## 2. Matrix Factorization
### **a. Representation via Inner Product of Embeddings**
```math
A_{uv} \approx z_v^T z_u
```
**Notation:**
- $A_{uv}$ – Entry of the adjacency matrix $A$ at row $u$, column $v$.
- $z_u, z_v$ – Embedding vectors of nodes $u$ and $v$.
- $z_v^T z_u$ – Inner product of the two embedding vectors.

### **b. Loss Function**
```math
\min_{Z} \sum_{i \in V, j \in V} (A_{ij} - z_j^T z_i)^2
```
**Notation:**
- $V$ – Set of nodes in the graph.
- $Z$ – Embedding matrix of nodes.
- $A_{ij}$ – Entry in the adjacency matrix.
- $(A_{ij} - z_j^T z_i)^2$ – Squared error term.

---

## 3. Graph Autoencoder (GAE)
### **a. Encoder**
```math
Z = GCN(X, A)
```
### **b. Decoder**
```math
\hat{A} = \sigma(Z^T Z)
```
### **c. Loss Function**
```math
\mathcal{L}_{BCE} = \sum_{i \in V, j \in V} -A_{ij} \log(\hat{A}_{ij}) - (1 - A_{ij}) \log(1 - \hat{A}_{ij})
```

---

## 4. Variational Graph Autoencoder (VGAE)
### **a. Encoder**
```math
\mathcal{N}(\mu_i, \sigma_i^2)
```
### **b. Loss Function**
```math
\mathcal{L}_{ELBO} = \mathcal{L}_{BCE} - KL[q(Z|X, A) \parallel p(Z)]
```
