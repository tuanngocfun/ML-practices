# **Graph Attention Networks (GATv2) - PyTorch Geometric**

## 📌 **Mục tiêu**
- **Tải và xử lý dataset Cora** từ PyTorch Geometric.
- **Xây dựng mô hình GATv2 với các cải tiến**.
- **Huấn luyện và đánh giá hiệu suất**.
- **Phân tích độ chính xác theo node degree**.

---

## 📥 **1. Cài đặt thư viện**
```python
# Cài đặt các thư viện cần thiết (chỉ chạy nếu chưa cài đặt)
!pip install torch torch-geometric scikit-learn
```

---

## 📌 **2. Tải Dataset Cora**
```python
import torch
from torch_geometric.datasets import Planetoid
from torch_geometric.utils import add_self_loops
from sklearn.preprocessing import StandardScaler

# Load dataset (Cora)
dataset = Planetoid(root=".", name="Cora")
data = dataset[0]

# Chuẩn hóa đặc trưng đầu vào
scaler = StandardScaler()
data.x = torch.tensor(scaler.fit_transform(data.x.numpy()), dtype=torch.float)

# Thêm self-loops vào đồ thị
data.edge_index, _ = add_self_loops(data.edge_index)

print(f"Dataset: {dataset.name}")
print(f"Số node: {data.x.shape[0]}, Số features: {data.x.shape[1]}")
print(f"Số cạnh: {data.edge_index.shape[1]}")
```

---

## 🏗 **3. Xây dựng mô hình GATv2**
```python
import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
from torch_geometric.nn import GATv2Conv

class GATv2Model(nn.Module):
    def __init__(self, dim_in, dim_h, dim_out, heads=8):
        super().__init__()
        self.gat1 = GATv2Conv(dim_in, dim_h, heads=heads)
        self.gat2 = GATv2Conv(dim_h * heads, dim_h, heads=heads)
        self.gat3 = GATv2Conv(dim_h * heads, dim_out, heads=1)

    def forward(self, x, edge_index):
        h = F.dropout(x, p=0.6, training=self.training)
        h = self.gat1(h, edge_index)
        h = F.elu(h)
        h = F.dropout(h, p=0.6, training=self.training)
        h = self.gat2(h, edge_index)
        h = F.elu(h)
        h = F.dropout(h, p=0.6, training=self.training)
        h = self.gat3(h, edge_index)
        return F.log_softmax(h, dim=1)
```

---

## 📌 **4. Hàm Huấn luyện với Early Stopping**
```python
    def fit(self, data, epochs=100, lr=0.005, weight_decay=0.0005):
        criterion = nn.CrossEntropyLoss()
        optimizer = optim.Adam(self.parameters(), lr=lr, weight_decay=weight_decay)
        scheduler = torch.optim.lr_scheduler.ReduceLROnPlateau(optimizer, mode='min', patience=5, factor=0.5, verbose=True)

        best_val_loss = float('inf')
        early_stop_patience = 10
        no_improve_count = 0

        self.train()
        for epoch in range(epochs):
            optimizer.zero_grad()
            out = self(data.x, data.edge_index)
            loss = criterion(out[data.train_mask], data.y[data.train_mask])
            loss.backward()
            optimizer.step()

            val_loss = criterion(out[data.val_mask], data.y[data.val_mask])
            scheduler.step(val_loss)

            print(f"Epoch {epoch+1}: Train Loss = {loss:.4f}, Val Loss = {val_loss:.4f}")

            # Early stopping
            if val_loss < best_val_loss:
                best_val_loss = val_loss
                no_improve_count = 0
            else:
                no_improve_count += 1
                if no_improve_count >= early_stop_patience:
                    print(f"Early stopping at epoch {epoch+1}")
                    break
```

---

## 🧪 **5. Đánh giá mô hình**
```python
@torch.no_grad()
def test(self, data):
    self.eval()
    out = self(data.x, data.edge_index)
    acc = (out.argmax(dim=1)[data.test_mask] == data.y[data.test_mask]).sum().item() / data.test_mask.sum().item()
    return acc

# Thêm phương thức test vào class
GATv2Model.test = test
```

---

## 🚀 **6. Khởi tạo mô hình & Huấn luyện**
```python
dim_in = dataset.num_features
dim_h = 32
dim_out = dataset.num_classes

gat = GATv2Model(dim_in, dim_h, dim_out)

print("Training GATv2...")
gat.fit(data, epochs=100)

# Kiểm tra độ chính xác trên tập test
test_acc = gat.test(data)
print(f"GATv2 Test Accuracy: {test_acc * 100:.2f}%")
```

---

## 📊 **7. Phân tích độ chính xác theo Node Degree**
```python
import matplotlib.pyplot as plt
import numpy as np
from torch_geometric.utils import degree

# Lấy đầu ra của mô hình
out = gat(data.x, data.edge_index)
degrees = degree(data.edge_index[0]).numpy()

accuracies = []
sizes = []

for i in range(0, 6):
    mask = np.where(degrees == i)[0]
    if len(mask) > 0:
        acc = (out.argmax(dim=1)[mask] == data.y[mask]).sum().item() / len(mask)
        accuracies.append(acc)
        sizes.append(len(mask))
    else:
        accuracies.append(0)
        sizes.append(0)

mask = np.where(degrees > 5)[0]
if len(mask) > 0:
    acc = (out.argmax(dim=1)[mask] == data.y[mask]).sum().item() / len(mask)
    accuracies.append(acc)
    sizes.append(len(mask))

# Vẽ biểu đồ
fig, ax = plt.subplots()
ax.set_xlabel('Node degree')
ax.set_ylabel('Accuracy score')
plt.bar(['0', '1', '2', '3', '4', '5', '6+'], accuracies)

for i in range(len(accuracies)):
    plt.text(i, accuracies[i], f'{accuracies[i] * 100:.2f}%', ha='center', color='black')
    plt.text(i, accuracies[i] / 2, sizes[i], ha='center', color='white')

plt.show()
```

---

## **🎯 Kết quả mong đợi**
- **Test Accuracy ~80% trên dataset Cora**.
- **Biểu đồ Accuracy theo Node Degree** hiển thị rõ ảnh hưởng của connectivity.

📌 **Bây giờ bạn có thể chạy toàn bộ notebook và kiểm tra kết quả! 🚀**
