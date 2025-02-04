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
GAE is an unsupervised model used to learn embeddings of nodes by compressing graph information into a latent space and reconstructing the adjacency matrix.

### **a. Encoder**
```math
Z = GCN(X, A)
```
**Notation:**
- $Z$ – Embedding matrix of nodes after encoding.
- $GCN(\cdot)$ – Graph Convolutional Network, a neural network model for feature extraction on graphs.
- $X$ – Feature matrix of nodes.
- $A$ – Adjacency matrix of the graph.

### **b. Decoder**
```math
\hat{A} = \sigma(Z^T Z)
```
**Notation:**
- $\hat{A}$ – Reconstructed adjacency matrix.
- $Z^T Z$ – Inner product of the embeddings.
- $\sigma(\cdot)$ – Sigmoid activation function.

### **c. Loss Function**
```math
\mathcal{L}_{BCE} = \sum_{i \in V, j \in V} -A_{ij} \log(\hat{A}_{ij}) - (1 - A_{ij}) \log(1 - \hat{A}_{ij})
```
**Notation:**
- $\mathcal{L}_{BCE}$ – Binary Cross-Entropy loss.
- $A_{ij}$ – True adjacency matrix value.
- $\hat{A}_{ij}$ – Predicted adjacency matrix value.

---

## 4. Variational Graph Autoencoder (VGAE)
VGAE is an improved version of GAE, where embeddings are learned as probabilistic distributions rather than fixed vectors.

### **a. Encoder (Embedding Distribution Representation)**
```math
\mathcal{N}(\mu_i, \sigma_i^2)
```
**Notation:**
- $\mathcal{N}(\mu_i, \sigma_i^2)$ – Gaussian distribution with mean $\mu_i$ and variance $\sigma_i^2$.
- $\mu_i$ – Mean of node embedding.
- $\sigma_i^2$ – Variance of node embedding.

### **b. Loss Function**
```math
\mathcal{L}_{ELBO} = \mathcal{L}_{BCE} - KL[q(Z|X, A) \parallel p(Z)]
```
**Notation:**
- $\mathcal{L}_{ELBO}$ – Evidence Lower Bound (ELBO), an optimization criterion for variational inference.
- $KL[q(Z|X, A) \parallel p(Z)]$ – Kullback–Leibler divergence, measuring the difference between the learned distribution $q(Z|X, A)$ and the prior distribution $p(Z)$.

---

## **5. Double-Radius Node Labeling (DRNL) and Deep Graph Convolutional Neural Network (DGCNN)**
Another approach to graph embedding is **Double-Radius Node Labeling (DRNL)**, used in models such as SEAL. DRNL assigns unique labels to nodes based on their distances to a pair of target nodes, allowing a GNN to learn effective representations for link prediction.

### **a. DRNL Function**
```math
f(i) = 1 + \min(d(i, x), d(i, y)) + \frac{d}{2} \left(\frac{d}{2} + (d\%2) - 1\right)
```
where $d = d(i, x) + d(i, y)$.

### **b. Integration with DGCNN**
- DRNL-labeled nodes are encoded using **one-hot encoding**.
- A **Deep Graph Convolutional Neural Network (DGCNN)** is then trained to predict links based on node labels and structural information.
- **Global Sort Pooling** ensures embeddings remain consistent across graph structures.
- The model is trained using **binary cross-entropy loss**.

This method complements the embedding-based techniques by leveraging structured node labeling to improve link prediction in graphs.

---

## **Comparison of GAE and VGAE**

| Model | Encoder | Decoder | Loss Function |
|-------|--------|---------|--------------|
| **GAE** | $Z = GCN(X, A)$ | $\hat{A} = \sigma(Z^T Z)$ | $\mathcal{L}_{BCE}$ |
| **VGAE** | $\mathcal{N}(\mu_i, \sigma_i^2)$ | $\hat{A} = \sigma(Z^T Z)$ | $\mathcal{L}_{ELBO}$ |

This document provides an in-depth mathematical and conceptual analysis of graph embedding methods, explaining key concepts and their associated formulas with detailed notation breakdowns.

