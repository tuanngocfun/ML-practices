# Graph Neural Networks (GNNs) and Message Passing Neural Networks (MPNNs)

## 1. Introduction to Message Passing Neural Network (MPNN)

MPNN is a general framework that unifies various Graph Neural Networks (GNNs). The process involves three main steps:

1. **Message Passing**: Nodes generate messages to their neighbors.
2. **Aggregation**: Messages from neighbors are aggregated.
3. **Update**: Node features are updated using aggregated messages.

### 1.1. Node-Level Update Equation
The standard message passing update rule is defined as:

```math
h_i' = \sum_{j \in \mathcal{N}_i} h_j W^T
```

where:
- $h_i$ is the feature embedding of node $i$.
- $\mathcal{N}_i$ is the set of neighboring nodes including itself.
- $W$ is the learnable weight matrix.

### 1.2. Generalized MPNN Equation

```math
h_i' = \gamma \left( h_i \oplus_{j \in \mathcal{N}_i} \varphi(h_i, h_j, e_{j,i}) \right)
```

where:
- $\varphi$ is the **message function** that computes the message from node $j$ to node $i$.
- $\oplus$ is the **aggregation function**, such as sum or mean.
- $\gamma$ is the **update function**, which updates the node representation.
- $e_{j,i}$ is the edge feature.

## 2. Graph Convolutional Network (GCN)

### 2.1. Normalized Aggregation Formula

```math
h_i' = \sum_{j \in \mathcal{N}_i} \frac{h_j}{\sqrt{deg(i)} \cdot \sqrt{deg(j)}} W
```

where:
- $deg(i)$ is the degree of node $i$, used for normalization.
- $W$ is a learnable weight matrix.

## 3. Heterogeneous Graphs

### 3.1. Definition
A **heterogeneous graph** consists of multiple types of nodes and edges. Formally defined as:

```math
G = (V, E), \quad \phi: V \to A, \quad \psi: E \to R
```

where:
- $V$ is the set of nodes.
- $E$ is the set of edges.
- $\phi$ is a function mapping nodes to their types.
- $\psi$ is a function mapping edges to their types.

### 3.2. Meta-Paths
Meta-paths define sequences of relations between different node types.

Examples:
- $U \to G \to U$ (Users playing the same game)
- $U \to G \to D \to G \to U$ (Users playing games by the same developer)

## 4. Graph Attention Network (GAT) for Heterogeneous Graphs

### 4.1. Attention Mechanism
For a heterogeneous GAT, attention weights are computed as:

```math
\alpha_{ij}^{\Phi_p} = \frac{\exp\left( \sigma \left(a_{\Phi_p}^{T} [W_{\Phi_p} h_i || W_{\Phi_p} h_j] \right) \right)}{\sum_{k \in \mathcal{N}_i^{\Phi_p}} \exp\left( \sigma \left(a_{\Phi_p}^{T} [W_{\Phi_p} h_i || W_{\Phi_p} h_k] \right) \right)}
```

where:
- $a_{\Phi_p}$ is the learnable attention vector.
- $W_{\Phi_p}$ is the weight matrix for meta-path $\Phi_p$.
- $\sigma$ is a non-linear function (e.g., LeakyReLU).

## 5. Hierarchical Attention Network (HAN)

### 5.1. Semantic-Level Attention

After obtaining embeddings from each meta-path, a second-level attention mechanism is applied to determine their importance:

```math
w_{\Phi_p} = \frac{1}{|V|} \sum_{i \in V} q^T \cdot \tanh\left(W \cdot z_{i}^{\Phi_p} + b \right)
```

where:
- $W$ and $q$ are learnable parameters.
- $z_{i}^{\Phi_p}$ is the embedding from meta-path $\Phi_p$.

### 5.2. Softmax Normalization

```math
\beta_{\Phi_p} = \frac{\exp(w_{\Phi_p})}{\sum_{k=1}^{P} \exp(w_{\Phi_k})}
```

where:
- $\beta_{\Phi_p}$ represents the importance of meta-path $\Phi_p$.

### 5.3. Final Embedding Combination

```math
Z = \sum_{p=1}^{P} \beta_{\Phi_p} \cdot Z_{\Phi_p}
```

where $Z$ is the final node embedding combining all meta-paths.

## 6. Conclusion
This document provides a detailed explanation of:
1. The fundamental equations for GNNs and MPNNs.
2. Handling heterogeneous graphs using meta-paths.
3. Attention mechanisms in GAT and HAN.
4. Implementing HAN using PyTorch Geometric.

These techniques allow for more effective learning from complex graphs with diverse node and edge types.
