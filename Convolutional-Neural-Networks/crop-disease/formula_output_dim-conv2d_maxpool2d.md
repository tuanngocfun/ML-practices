# 📐 Formula for Calculating Output Size in Conv2d and MaxPool2d Layers

## 🎯 **General Formula for Output Dimension (Conv2d/MaxPool2d)**

For both `Conv2d` and `MaxPool2d`, the output height/width is calculated as:

```math
Output size = floor((Input size + 2 * padding - kernel size) / stride) + 1
```

### 🔑 **Variables Explained:**

| Term             | Meaning                                                         |
|------------------|-----------------------------------------------------------------|
| **Input size**   | Size of input dimension (height or width)                       |
| **Kernel size**  | Size of convolution/pooling kernel (usually square, e.g., 3x3)  |
| **Padding**      | Number of padding pixels added to each side of input            |
| **Stride**       | Step size at which the kernel moves                            |
| **Floor**        | The result is rounded down to the nearest integer              |

---

## ✅ **Example for Conv2d Layer**

```python
nn.Conv2d(in_channels=3, out_channels=64, kernel_size=7, stride=2, padding=3)
```

### If input size is `224 x 224`:

```math
Output size = floor((224 + 2*3 - 7) / 2) + 1
            = floor(223 / 2) + 1
            = 111 + 1 = 112
```

➡️ **Output size**: `112 x 112`, and output channels = 64.

---

## ✅ **Example for MaxPool2d Layer**

```python
nn.MaxPool2d(kernel_size=3, stride=2, padding=1)
```

### If input size is `112 x 112`:

```math
Output size = floor((112 + 2*1 - 3) / 2) + 1
            = floor(111 / 2) + 1
            = 55 + 1 = 56
```

➡️ **Output size**: `56 x 56`, channels remain the same as before.

---

## ✅ **Final Flattened Tensor for Linear Layer**

If final Conv2d layer output shape is `(batch_size, channels, height, width)`:

To **Flatten**, total features:

```math
features = channels * height * width
```

### Example:

From `(1, 256, 14, 14)`:

```math
Flattened size = 256 * 14 * 14 = 50176
```

So `in_features=50176` when connecting to a `Linear` layer.

---

## 🚨 **Important Notes:**

1. **Each Conv2d/MaxPool2d affects spatial size** — use formula step by step.
2. **Padding** helps maintain spatial size if needed (e.g., `padding=1` for `kernel_size=3` keeps size).
3. After **Flatten**, you must use the correct total number of features to connect to `Linear`.

---

## ✅ **Final Recap of Formula:**

```math
Output size = floor((Input size + 2 * padding - kernel size) / stride) + 1
```

