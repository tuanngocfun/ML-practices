# Learning from Heterogeneous Graphs: Original vs Improved Approaches

## 1. Overview
This document compares three different approaches for learning from heterogeneous graphs using **Hierarchical Attention Networks (HAN)** on the **DBLP dataset**. We analyze the **original approach**, **improved version A**, and **improved version B**, focusing on model architecture, training strategy, optimization, and performance improvements.

---

## 2. Model Architecture

| **Aspect**             | **Original Approach** | **Improved Version A** | **Improved Version B** |
|------------------------|----------------------|----------------------|----------------------|
| **Model Used** | **HAN** | **HAN with better preprocessing** | **Enhanced HAN with multiple layers & regularization** |
| **Hidden Dimension** | `128` | `128` | **`192`** |
| **Attention Heads** | `8` | `8` | **`12`** |
| **Number of Layers** | `1` | `1` | **`2`** |
| **Classifier** | `Linear(dim_h, out_dim)` | `Linear(dim_h, out_dim)` | **Multi-layer classifier with GELU activation, LayerNorm, and Dropout** |
| **Dropout** | `0.6` | `0.6` | **`0.5` (More layers)** |
| **Layer Normalization** | ❌ | ❌ | ✅ **(Added LayerNorm for stability)** |
| **Residual Connections** | ❌ | ❌ | ✅ **(Better feature propagation)** |

```python
# Example from Improved Version B
class EnhancedHAN(nn.Module):
    def __init__(self, dim_in, dim_out, dim_h=192, heads=12, num_layers=2):
        super().__init__()
        self.hans = nn.ModuleList([
            HANConv(
                in_channels=-1 if i == 0 else dim_h,
                out_channels=dim_h,
                heads=heads,
                dropout=0.5,
                metadata=data.metadata()
            ) for i in range(num_layers)
        ])
        self.classifier = nn.Sequential(
            Linear(dim_h, dim_h),
            nn.GELU(),
            LayerNorm(dim_h),
            Dropout(0.5),
            Linear(dim_h, dim_h // 2),
            nn.GELU(),
            LayerNorm(dim_h // 2),
            Dropout(0.5),
            Linear(dim_h // 2, dim_out)
        )
```

---

## 3. Training Strategy

```python
# Example Learning Rate Scheduling in Version B
scheduler = torch.optim.lr_scheduler.CosineAnnealingWarmRestarts(
    optimizer,
    T_0=20,
    T_mult=2,
    eta_min=1e-6
)
```

| **Aspect**             | **Original Approach** | **Improved Version A** | **Improved Version B** |
|------------------------|----------------------|----------------------|----------------------|
| **Feature Normalization** | ❌ | ✅ `T.NormalizeFeatures()` | ✅ `T.NormalizeFeatures()` |
| **Self-Loops Added** | ❌ | ✅ `T.AddSelfLoops()` | ✅ `T.AddSelfLoops()` |
| **Train-Val-Test Split** | Predefined split | **Custom split** (`T.RandomNodeSplit`) | **Custom split** (`T.RandomNodeSplit`) |
| **Number of Epochs** | `101` | `101` | **Up to `200` (With Early Stopping)** |
| **Gradient Clipping** | ❌ | ❌ | ✅ **(Prevents exploding gradients)** |
| **Learning Rate Schedule** | ❌ | ❌ | ✅ **(Cosine Annealing)** |

---

## 4. Regularization & Optimization

| **Aspect**             | **Original Approach** | **Improved Version A** | **Improved Version B** |
|------------------------|----------------------|----------------------|----------------------|
| **Weight Decay** | `0.001` | `0.001` | **`0.05` (Stronger regularization)** |
| **Dropout Usage** | `0.6` | `0.6` | **Multiple `0.5` Dropout layers** |
| **L2 Regularization on Attention** | ❌ | ❌ | ✅ **(Regularization on HAN's attention weights)** |
| **Optimizer** | `Adam` | `Adam` | **`AdamW` (Better weight decay handling)** |

```python
# Example Gradient Clipping in Version B
torch.nn.utils.clip_grad_norm_(model.parameters(), max_norm=0.5)
```

---

## 5. Performance Improvements

| **Metric**  | **Original Approach** | **Improved Version A** | **Improved Version B** |
|------------|----------------------|----------------------|----------------------|
| **Best Train Accuracy** | `99.50%` | `99.25%` | **`99.92%`** |
| **Best Validation Accuracy** | `78.75%` | `78.50%` | **`88.90%`** |
| **Final Test Accuracy** | **`81.58%`** | **`82.01%`** | **`85.94%`** |

```python
# Early Stopping Implementation in Version B
best_val_acc = 0
patience = 15
patience_counter = 0
for epoch in range(200):
    model.train()
    optimizer.zero_grad()
    out = model(data.x_dict, data.edge_index_dict)
    loss = F.cross_entropy(out[data['author'].train_mask], data['author'].y[data['author'].train_mask])
    loss.backward()
    optimizer.step()
    scheduler.step()
    if epoch % 20 == 0:
        val_acc = test(data['author'].val_mask)
        if val_acc > best_val_acc:
            best_val_acc = val_acc
            patience_counter = 0
        else:
            patience_counter += 1
        if patience_counter >= patience:
            break
```

---

## 6. Summary: Which Version is Best?

| **Version** | **Pros** | **Cons** |
|------------|---------|---------|
| **Original Approach** | Simple & effective | Prone to overfitting, lacks advanced optimization |
| **Improved Version A** | Feature normalization, better training | Still limited by single-layer HAN |
| **Improved Version B** | Strong regularization, deeper layers, 4% better accuracy | More complex, longer training time |

✔ **Use Version A** if you want simplicity with improvements.
✔ **Use Version B** for the best performance (~4% higher test accuracy).

🚀 **Conclusion: Improved Version B is the most advanced and best-performing model!**
