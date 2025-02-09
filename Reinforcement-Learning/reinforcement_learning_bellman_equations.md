# Giải thích chi tiết và lời giải cho Bài tập Bellman Optimality

## 1. Giới thiệu

Trong bài viết này, chúng ta sẽ giải thích chi tiết và trình bày các bài tập liên quan đến phương trình Bellman tối ưu. Chúng ta sẽ tập trung vào việc làm rõ các khái niệm quan trọng, cách đọc hiểu công thức, và giải bài tập cụ thể.

---

## 2. Phương trình Bellman tối ưu cho Robot Tái Chế

Chúng ta xét mô hình một robot tái chế, trong đó:

- **Trạng thái**: High (**h**) và Low (**l**)
- **Hành động**:
  - **s**: Search (tìm kiếm)
  - **w**: Wait (chờ)
  - **re**: Recharge (sạc pin)

Hệ phương trình Bellman cho giá trị tối ưu $v_*(h)$ (trạng thái High):

```math
v_*(h)=\max\begin{cases}
p(h|h,s)[r(h,s,h)+\gamma v_(h)]+p(l|h,s)[r(h,s,l)+\gamma v_(l)],\
p(h|h,w)[r(h,w,h)+\gamma v_(h)]+p(l|h,w)[r(h,w,l)+\gamma v_(l)],\
p(h|h,re)[r(h,re,h)+\gamma v_(h)]+p(l|h,re)[r(h,re,l)+\gamma v_*(l)]
\end{cases}
```

Rút gọn bằng các ký hiệu:

- $\alpha=p(h|h,s)$: Xác suất giữ nguyên trạng thái High khi Search
- $1-\alpha=p(l|h,s)$: Xác suất chuyển sang Low khi Search
- $r_s=r(h,s,h)=r(h,s,l)$: Phần thưởng khi Search
- $r_w=r(h,w,h)$: Phần thưởng khi Wait

Sau khi thay vào, phương trình rút gọn:

```math
v_*(h) = \max \begin{cases}
    \alpha [r_s + \gamma v_*(h)] + (1 - \alpha) [r_s + \gamma v_*(l)], \\
    r_w + \gamma v_*(h)
\end{cases}
```

Tương tự, phương trình Bellman tối ưu cho $v_*(l)$ (trạng thái Low):

```math
v_*(l) = \max \begin{cases}
    \beta r_s - 3(1 - \beta) + \gamma [(1 - \beta) v_*(h) + \beta v_*(l)], \\
    r_w + \gamma v_*(l), \\
    \gamma v_*(h)
\end{cases}
```

Trong đó:

- $\beta=p(l|l,s)$: Xác suất giữ nguyên trạng thái Low khi Search
- $1-\beta=p(h|l,s)$: Xác suất chuyển sang High khi Search
- $-3$: Phạt khi chuyển sang High do hết pin

---

## 3. Cách giải bài tập Bellman Optimality

### **Exercise 3.20 - 3.29**

#### **Bài tập 3.20**: Mô tả hoặc vẽ hàm giá trị tối ưu của bài toán golf.
- Hàm giá trị tối ưu trong golf phụ thuộc vào số bước cần để đưa bóng vào lỗ.

#### **Bài tập 3.21**: Mô tả hoặc vẽ các đường đồng mức của hàm giá trị tối ưu khi sử dụng putter.
- Các đường đồng mức phản ánh số cú đánh tối thiểu cần để đến đích.

#### **Bài tập 3.22**: Xác định chính sách tối ưu cho bài toán MDP tiếp diễn với các hành động trái/phải.
- Xét các giá trị phần thưởng ở mỗi nhánh và tìm chính sách tối ưu dựa trên $\gamma$.

#### **Bài tập 3.23**: Viết phương trình Bellman cho $q_*$ của bài toán robot tái chế.
- Biểu diễn hàm hành động-giá trị theo xác suất chuyển tiếp và phần thưởng.

#### **Bài tập 3.24**: Tính toán giá trị trạng thái tối ưu nhất trong Gridworld và làm tròn đến 3 chữ số.
- Áp dụng phương trình Bellman để tính toán chính xác hơn từ giá trị đã biết.

#### **Bài tập 3.25**: Viết phương trình Bellman cho $v_*$ theo $q_*$.
- Sử dụng mối quan hệ giữa $v_*$ và giá trị hành động tối ưu.

#### **Bài tập 3.26**: Viết phương trình Bellman cho $q_*$ theo $v_*$ và xác suất chuyển tiếp.
- Thay thế các phần thưởng và xác suất vào công thức tổng quát.

#### **Bài tập 3.27 - 3.29**:
- Phát biểu chính sách tối ưu theo $q_*$.
- Viết phương trình Bellman tổng quát cho $\pi_*$ theo các giá trị trạng thái.
- Xây dựng hệ phương trình đầy đủ.

---

## 4. Kết luận

Bài viết này giúp bạn hiểu rõ cách xây dựng và giải quyết hệ phương trình Bellman cho bài toán tối ưu trong MDP và Reinforcement Learning. Việc hiểu rõ những nguyên lý này là tiền đề cho các phương pháp học tăng cường tiên tiến như Q-learning và Policy Iteration.

Việc áp dụng các phương trình này giúp xác định chính sách tối ưu trong các môi trường quyết định tuần tự, từ đó tối ưu hóa các chiến lược ra quyết định một cách hiệu quả.

**Bài tập 3.20:**

### Hàm giá trị trạng thái tối ưu cho bài toán golf

Hàm giá trị trạng thái tối ưu, ký hiệu là $v_*(s)$, đại diện cho giá trị mong đợi khi bắt đầu từ trạng thái $s$ và đi theo chính sách tối ưu $\pi_*$.

- **Trạng thái "bóng vào lỗ"**: Khi bóng đã vào lỗ, không cần thực hiện cú đánh nào nữa, nên giá trị $v_*(s) = 0$.
- **Trạng thái trên green**: Chính sách tối ưu gợi ý sử dụng gậu putter để đưa bóng vào lỗ chỉ với một cú đánh, nên $v_*(s) = -1$.
- **Trạng thái xa hơn**: Nếu bóng cách xa green hơn, số cú đánh tối thiểu để đưa bóng vào lỗ sẽ tăng lên, dẫn đến giá trị $v_*(s) = -2, -3, \dots$, phản ánh số cú đánh cần thiết.
- **Trạng thái chướng ngại vật (ví dụ: bãy cát, nước)**: Có thể có giá trị thấp hơn đáng kể do độ khó thoát khỏi khu vực này.

Hàm giá trị tối ưu được xác định bởi công thức:

```math
v_*(s) = \max_a q_*(s, a)
```

trong đó $q_*(s, a)$ là hàm giá trị hành động tối ưu, thể hiện giá trị mong đợi khi thực hiện hành động $a$ tại trạng thái $s$ và sau đó đi theo chính sách tối ưu.

---

**Bài tập 3.21:**

### Hàm giá trị hành động tối ưu $q_*(s, \text{putter})$ cho bài toán golf

Hàm giá trị hành động $q_*(s, a)$ đại diện cho giá trị mong đợi khi thực hiện hành động $a$ tại trạng thái $s$, sau đó đi theo chính sách tối ưu. Khi sử dụng gậu putter:

```math
q_*(s, \text{putter}) = r + \gamma v_*(s')
```

trong đó:
- $s'$ là trạng thái sau khi thực hiện cú đánh.
- $r$ là phần thưởng tức thời nhận được (thường là $-1$ vì mỗi cú đánh đều bị trừ điểm).
- $\gamma$ là hệ số chiết khấu, thể hiện mức độ quan tâm đến phần thưởng trong tương lai.

Các đường đồng mức của $q_*(s, \text{putter})$ cho thấy ảnh hưởng của việc sử dụng putter từ các vị trí khác nhau đến kết quả cuối cùng.

---

**Bài tập 3.22:**

### Chính sách tối ưu với các giá trị $\gamma$ khác nhau

Chính sách tối ưu $\pi_*(s)$ phụ thuộc vào hệ số chiết khấu $\gamma$:

- **Khi $\gamma = 0.2$**: Tác nhân tập trung vào phần thưởng ngay lập tức, ưu tiên hành động có phần thưởng tức thời cao nhất mà không quan tâm nhiều đến phần thưởng tương lai.
- **Khi $\gamma = 0.9$**: Tác nhân quan tâm đến phần thưởng dài hạn, lựa chọn hành động có lợi ích tổng thể lớn nhất trong tương lai.

Công thức để tính chính sách tối ưu là:

```math
\pi_*(s) = \arg\max_a \sum_{s', r} p(s', r | s, a) [r + \gamma v_*(s')]
```

trong đó $p(s', r | s, a)$ là xác suất chuyển từ trạng thái $s$ sang $s'$ và nhận phần thưởng $r$ khi thực hiện hành động $a$.

---

**Bài tập 3.23:**

### Phương trình Bellman cho $q_*$ trong bài toán robot tái chế

Trong bài toán robot tái chế, robot cần quyết định giữa việc tiếp tục sử dụng một vật liệu cũ hay tái chế nó. Giá trị của hành động $q_*(s, a)$ được xác định dựa trên các phần thưởng mong đợi và các trạng thái kế tiếp có thể xảy ra.

```math
q_*(s, a) = \sum_{s', r} p(s', r | s, a) [r + \gamma \max_{a'} q_*(s', a')]
```

trong đó:
- $p(s', r | s, a)$ là xác suất chuyển từ trạng thái $s$ sang trạng thái $s'$ và nhận phần thưởng $r$ sau khi thực hiện hành động $a$.
- $r$ là phần thưởng tức thời nhận được khi thực hiện hành động $a$.
- $\gamma$ là hệ số chiết khấu, xác định mức độ quan tâm đến phần thưởng tương lai.
- $\max_{a'} q_*(s', a')$ đảm bảo rằng hành động tốt nhất tại trạng thái $s'$ sẽ được lựa chọn để tối ưu hóa tổng giá trị mong đợi.

Phương trình trên cho thấy rằng giá trị của một hành động được tính bằng cách xem xét tất cả các trạng thái kế tiếp có thể xảy ra, cân nhắc phần thưởng nhận được, và tính toán giá trị tối ưu của các hành động tiếp theo.

---

**Bài tập 3.24:**

### Kiểm tra giá trị tối ưu trong bài toán Gridworld

#### Mục tiêu:

- Xác minh giá trị tối ưu của trạng thái tốt nhất trong bài toán Gridworld có giá trị $24.4$.
- Sử dụng phương trình Bellman tối ưu $v_*(s)$ để tính toán giá trị này.

#### 1. Phương trình Bellman tối ưu cho trạng thái

Phương trình Bellman tối ưu cho giá trị trạng thái tối ưu $v_*(s)$ là:

```math
v_*(s) = \max_{a} \sum_{s',r} p(s',r | s,a) [r + \gamma v_*(s')]
```

Trong đó:
- $v_*(s)$ là giá trị tối ưu của trạng thái $s$.
- $a$ là hành động tối ưu tại $s$.
- $p(s',r | s,a)$ là xác suất chuyển từ trạng thái $s$ sang $s'$ với phần thưởng $r$.
- $\gamma$ là hệ số chiết khấu (discount factor).

#### 2. Áp dụng vào bài toán Gridworld

**Dữ liệu từ bài toán:**

- Trạng thái tối ưu nhất có $v_*(s) = 24.4$.
- Phần thưởng nhận được khi đi đúng hướng là $r = 10$.
- Hệ số chiết khấu $\gamma = 0.9$.
- Giả sử hành động tối ưu chuyển đến trạng thái $s'$ với xác suất $1$.

Theo phương trình Bellman tối ưu:

```math
v_*(s) = r + \gamma v_*(s')
```

Thay số:

```math
24.4 = 10 + 0.9 v_*(s')
```

Giải ra:

```math
v_*(s') = \frac{24.4 - 10}{0.9} = \frac{14.4}{0.9} = 16.0
```

Vậy giá trị tối ưu của trạng thái tiếp theo $s'$ phải là $16.0$ để đảm bảo trạng thái trước đó có giá trị $24.4$.

#### 3. Câu hỏi mở rộng

- Nếu $\gamma$ giảm xuống $0.8$, giá trị tối ưu sẽ thay đổi như thế nào?
- Nếu phần thưởng $r$ thay đổi theo trạng thái, làm sao để cập nhật phương trình Bellman?
- Làm thế nào để kiểm tra các trạng thái khác trong Gridworld tuân theo phương trình Bellman tối ưu?

---

**Bài tập 3.25:**

### Mối quan hệ giữa $v_*$ và $q_*$

Hàm giá trị trạng thái tối ưu $v_*(s)$ có thể được suy ra từ hàm giá trị hành động tối ưu $q_*(s, a)$:

```math
v_*(s) = \max_{a \in A(s)} q_*(s, a)
```

Điều này có nghĩa là giá trị tối ưu của một trạng thái là giá trị cao nhất trong tất cả các hành động có thể thực hiện từ trạng thái đó. Nói cách khác, tác nhân sẽ luôn chọn hành động có giá trị lớn nhất để tối ưu hóa phần thưởng trong dài hạn.

Trong học tăng cường, mối quan hệ này rất quan trọng đối với các thuật toán như Value Iteration và Q-Learning, trong đó tác nhân liên tục cập nhật hàm giá trị trạng thái và hành động dựa trên các quan sát mới.

---

**Bài tập 3.26:**

### Biểu diễn $q_*$ theo $v_*$ và xác suất chuyển trạng thái

Giá trị hành động tối ưu có thể được biểu diễn dựa trên giá trị trạng thái tối ưu bằng phương trình Bellman:

```math
q_*(s, a) = \sum_{s', r} p(s', r | s, a) [r + \gamma v_*(s')]
```

trong đó:
- $p(s', r | s, a)$ là xác suất chuyển từ trạng thái $s$ sang trạng thái $s'$ và nhận phần thưởng $r$ khi thực hiện hành động $a$.
- $r$ là phần thưởng ngay lập tức.
- $\gamma$ là hệ số chiết khấu.
- $v_*(s')$ là giá trị tối ưu của trạng thái kế tiếp $s'$.

Phương trình này cho thấy rằng giá trị của một hành động tại trạng thái hiện tại phụ thuộc vào tất cả các trạng thái kế tiếp có thể xảy ra, phần thưởng ngay lập tức, và giá trị kỳ vọng của các trạng thái kế tiếp.

Công thức này đóng vai trò quan trọng trong học tăng cường, đặc biệt trong các thuật toán như Policy Iteration và Q-learning, nơi tác nhân sử dụng các giá trị trạng thái và xác suất chuyển trạng thái để cập nhật chiến lược học tập của mình.

---

**Bài tập 3.27:**

### Biểu diễn chính sách tối ưu $\pi_*$ theo $q_*$

Chính sách tối ưu $\pi_*(s)$ tại một trạng thái $s$ sẽ chọn hành động tối đa hóa hàm giá trị hành động $q_*(s, a)$:

```math
\pi_*(s) = \arg\max_{a \in A(s)} q_*(s, a)
```

Phương trình này nói rằng chính sách tối ưu luôn chọn hành động mang lại giá trị kỳ vọng cao nhất. Điều này đảm bảo rằng tác nhân luôn theo đuổi chiến lược mang lại phần thưởng tối ưu dựa trên các giá trị hiện có của hàm hành động.

Trong học tăng cường (reinforcement learning), công thức này được sử dụng trong bước cải thiện chính sách, như trong thuật toán Policy Iteration, nơi chính sách được cập nhật theo từng bước để tối đa hóa phần thưởng mong đợi.

---

**Bài tập 3.28:**

### Biểu diễn chính sách tối ưu $\pi_*$ theo $v_*$ và xác suất chuyển trạng thái

Thay vì định nghĩa $\pi_*$ trực tiếp theo $q_*$, ta có thể biểu diễn nó theo hàm giá trị trạng thái tối ưu $v_*(s)$ và xác suất chuyển trạng thái:

```math
\pi_*(s) = \arg\max_{a \in A(s)} \sum_{s', r} p(s', r | s, a) [r + \gamma v_*(s')]
```

trong đó:
- $p(s', r | s, a)$ là xác suất chuyển sang trạng thái $s'$ và nhận phần thưởng $r$ khi thực hiện hành động $a$ từ trạng thái $s$.
- $r$ là phần thưởng tức thời.
- $\gamma$ là hệ số chiết khấu.
- $v_*(s')$ là giá trị trạng thái tối ưu của trạng thái kế tiếp.

Công thức này rất quan trọng trong các phương pháp lập trình động như Policy Iteration và Value Iteration, giúp xác định chính sách tối ưu dựa trên giá trị mong đợi của các phần thưởng trong tương lai.

---

**Bài tập 3.29:**

### Giải bài tập 3.29 - Viết lại các phương trình Bellman

Bài tập yêu cầu chúng ta viết lại bốn phương trình Bellman cho các hàm giá trị:

1. **Giá trị trạng thái theo chính sách** $v_\pi(s)$
2. **Giá trị trạng thái tối ưu** $v_*(s)$
3. **Giá trị hành động theo chính sách** $q_\pi(s, a)$
4. **Giá trị hành động tối ưu** $q_*(s, a)$

Những phương trình này cần được biểu diễn bằng:

- **Hàm xác suất chuyển tiếp** $p(s',r | s,a)$
- **Hàm phần thưởng** $r(s,a)$

---

### 1. Phương trình Bellman cho giá trị trạng thái theo chính sách $v_\pi(s)$

Hàm giá trị trạng thái theo chính sách $\pi$ được tính theo kỳ vọng của phần thưởng nhận được khi tác nhân đi theo chính sách đó:

```math
v_\pi(s) = \sum_{a \in A(s)} \pi(a | s) \sum_{s', r} p(s', r | s, a) [r + \gamma v_\pi(s')]
```

Diễn giải:
- $\pi(a | s)$ là xác suất chọn hành động $a$ tại trạng thái $s$.
- $p(s', r | s, a)$ là xác suất chuyển từ trạng thái $s$ đến $s'$, nhận phần thưởng $r$.
- $r + \gamma v_\pi(s')$ là giá trị mong đợi khi thực hiện hành động $a$.

---

### 2. Phương trình Bellman tối ưu cho giá trị trạng thái $v_*(s)$

Giá trị tối ưu của trạng thái $s$ là giá trị lớn nhất có thể đạt được bằng cách chọn hành động tối ưu:

```math
v_*(s) = \max_{a \in A(s)} \sum_{s', r} p(s', r | s, a) [r + \gamma v_*(s')]
```

Diễn giải:
- Thay vì sử dụng chính sách $\pi$, ta chọn hành động tối ưu $a^*$.
- Giá trị tối ưu của trạng thái $s$ được tính dựa trên hành động tốt nhất tại mỗi bước.

---

### 3. Phương trình Bellman cho giá trị hành động theo chính sách $q_\pi(s, a)$

Giá trị của hành động $a$ tại trạng thái $s$ được tính bằng kỳ vọng của phần thưởng và giá trị trạng thái tương lai khi đi theo chính sách $\pi$:

```math
q_\pi(s, a) = \sum_{s', r} p(s', r | s, a) [r + \gamma \sum_{a'} \pi(a' | s') q_\pi(s', a')]
```

Diễn giải:
- $q_\pi(s, a)$ tính giá trị của hành động $a$ tại trạng thái $s$.
- Không cần cộng dồn qua tất cả các hành động tại $s$ vì ta đang xét một hành động cụ thể $a$.

---

### 4. Phương trình Bellman tối ưu cho giá trị hành động $q_*(s, a)$

Phương trình Bellman tối ưu cho giá trị hành động tương tự như $q_\pi(s, a)$, nhưng thay vì dùng $v_\pi(s')$, ta sử dụng giá trị tối ưu $v_*(s')$:

```math
q_*(s, a) = \sum_{s', r} p(s', r | s, a) [r + \gamma \max_{a'} q_*(s', a')]
```

Diễn giải:
- Khi đã thực hiện hành động $a$ tại trạng thái $s$, ta tiếp tục chọn hành động tối ưu $a'$ tại trạng thái tiếp theo $s'$.
- Việc lấy giá trị tối đa $\max_{a'} q_*(s', a')$ đảm bảo rằng ta luôn theo chính sách tối ưu.

---

### 5. Mối quan hệ quan trọng giữa các phương trình

- Giữa $v_\pi(s)$ và $q_\pi(s, a)$:

```math
v_\pi(s) = \sum_{a} \pi(a | s) q_\pi(s, a)
```

- Giữa $v_*(s)$ và $q_*(s, a)$:

```math
v_*(s) = \max_{a} q_*(s, a)
```

- Giữa chính sách tối ưu $\pi_*$ và $q_*$:

```math
\pi_*(s) = \arg\max_{a} q_*(s, a)
```

---

### 6. Kết luận

Bài tập 3.29 yêu cầu viết lại các phương trình Bellman bằng cách:

- Biểu diễn chúng theo xác suất chuyển tiếp $p(s',r | s,a)$ và phần thưởng $r(s,a)$.
- Phân biệt rõ giá trị trạng thái $v$ và giá trị hành động $q$.
- Chỉ ra sự khác biệt giữa chính sách bất kỳ $\pi$ và chính sách tối ưu $\pi_*$. 

Các phương trình trên là nền tảng của **Dynamic Programming (DP)** và **Reinforcement Learning (RL)**, giúp xác định chính sách tối ưu trong các bài toán ra quyết định tuần tự.

---

Những công thức này đóng vai trò quan trọng trong học tăng cường, là nền tảng của các thuật toán như Q-learning và Deep Q-Networks (DQN), trong đó tác nhân học cách ước lượng giá trị của các hành động và cập nhật chính sách để tối ưu hóa phần thưởng trong dài hạn.