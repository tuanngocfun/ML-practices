# Giải Bài Tập Exercise 5.4 - Monte Carlo ES

## 1. Giới Thiệu
Bài tập yêu cầu chúng ta tối ưu hóa thuật toán **Monte Carlo ES (Exploring Starts)** bằng cách sử dụng phương pháp cập nhật trung bình động thay vì lưu danh sách tất cả giá trị hồi quy ($mathG$).

---

## 2. Công Thức Trung Bình Động
Từ Chương 2.4, công thức cập nhật giá trị trung bình có thể được triển khai như sau:

```math
Q_{n+1} = \frac{1}{n} \sum_{i=1}^{n} R_i
```

Mở rộng công thức:

```math
Q_{n+1} = \frac{1}{n} (R_n + \sum_{i=1}^{n-1} R_i)
```

Sử dụng phương pháp thế:

```math
Q_{n+1} = \frac{1}{n} (R_n + (n-1)Q_n)
```

Ta rút gọn được:

```math
Q_{n+1} = Q_n + \frac{1}{n} (R_n - Q_n)
```

---

## 3. Áp Dụng Cho Monte Carlo ES
Tương tự, áp dụng công thức trung bình động cho giá trị hành động $mathQ_n(S_t, A_t)$:

```math
Q_n(S_t, A_t) = \frac{1}{n} \sum_{i=1}^{n} G_i(S_t, A_t)
```

Mở rộng công thức:

```math
Q_n(S_t, A_t) = \frac{1}{n} (G_n(S_t, A_t) + \sum_{i=1}^{n-1} G_i(S_t, A_t))
```

Thế giá trị trung bình động:

```math
Q_n(S_t, A_t) = \frac{1}{n} (G_n(S_t, A_t) + (n-1) Q_{n-1}(S_t, A_t))
```

Rút gọn công thức cập nhật:

```math
Q_n(S_t, A_t) = Q_{n-1}(S_t, A_t) + \frac{1}{n} (G_n(S_t, A_t) - Q_{n-1}(S_t, A_t))
```

---

## 4. Lợi Ích Của Phương Pháp Cập Nhật Mới
- ✅ **Không cần lưu danh sách tất cả các giá trị $mathG$ cho mỗi cặp (S, A)**.
- ✅ **Bộ nhớ tối ưu hơn** vì chỉ cần lưu trữ giá trị trung bình hiện tại.
- ✅ **Cập nhật giá trị hành động nhanh hơn**, giúp hội tụ chính sách hiệu quả.

---

## 5. Kết Luận
Bằng cách sử dụng công thức cập nhật trung bình động thay vì lưu toàn bộ lịch sử phần thưởng, Monte Carlo ES có thể được cải thiện để trở nên **hiệu quả hơn về bộ nhớ và tốc độ tính toán**.

