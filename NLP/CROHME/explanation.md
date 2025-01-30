# Detailed Code Explanation, Math Representation, and Pseudocode for LOS Algorithm

## **1. Overview**
This implementation models a stroke-level graph structure using a **Line of Sight (LOS)** algorithm. Strokes are represented as sequences of points in a 2D plane. An edge exists between two strokes if one stroke has an unobstructed line of sight to another based on angular visibility.

The algorithm uses trigonometric calculations and interval arithmetic to determine visibility.

---

## **2. Key Mathematical Representations**

### **2.1 Center of a Stroke**
The center of a stroke is calculated as the midpoint of its bounding box:

```math
Center(S) = [(x_{min} + x_{max}) / 2, (y_{min} + y_{max}) / 2]
```

Where:
- $x_min$ and $x_max$ are the minimum and maximum $x-coordinates$ of the stroke points.
- $y_min$ and $y_max$ are the minimum and maximum $y-coordinates$ of the stroke points.

### **2.2 Angular Range**
For each stroke $j$, the angular range it subtends from the center $sc$ of stroke $i$ is defined as:

```math
\theta = \cos^{-1} \left( \frac{\mathbf{w} \cdot \mathbf{h}}{\| \mathbf{w} \| \| \mathbf{h} \|} \right)
```

Where:
- $\mathbf{w}$ is the vector from $sc$ to a point on stroke $j$.
- $\mathbf{h}$ is the horizontal reference vector $[1, 0]$.

Adjust $\theta$ based on the position of $\mathbf{w}$:
- If $w_y < 0$, then $\theta = 2\pi - \theta$.

The range for stroke $j$ is given as:

```math
[\theta_{\text{min}}, \theta_{\text{max}}]
```

### **2.3 Visibility**
Visibility is determined by checking the intersection of the angular range $U$ with $[\theta_{\text{min}}, \theta_{\text{max}}]$:

```math
V = U \cap [\theta_{\text{min}}, \theta_{\text{max}}]
```

If $V$ is non-empty, the strokes are visible to each other, and an edge is added.

---

## **3. Pseudocode**

### **Main Algorithm: `LOS`**
```plaintext
Input: List of strokes, each stroke is a list of 2D points.
Output: Adjacency matrix representing visibility graph.

1. Initialize `edges` as a size x size zero matrix.
2. For each stroke `i`:
    a. Compute the center `sc` of stroke `i`.
    b. Set `U` to the interval [0, 2π].
    c. Sort all strokes by their distance to `sc`.
    d. For each stroke `j` (in sorted order):
        i. If `i` != `j`, initialize `min_theta` to ∞ and `max_theta` to -∞.
       ii. For each point `n` in stroke `j`:
            A. Compute vector `w = n - sc`.
            B. Calculate angle `theta` using the dot product:
               - If `w_y >= 0`, use θ = acos(w·h / ||w|| ||h||).
               - Else, use θ = 2π - acos(w·h / ||w|| ||h||).
            C. Update `min_theta` and `max_theta` with θ.
      iii. Define interval `h = [min_theta, max_theta]`.
       iv. Compute `V = U ∩ h`.
        v. If `V` is not empty:
            A. Set `edges[i][j] = 1` and `edges[j][i] = 1`.
            B. Update `U = U - h`.
3. Return `edges`.
```

### **Helper Functions**

#### `calculate_center`
```plaintext
Input: List of points.
Output: Center point of the bounding box.

1. Initialize `left`, `right`, `top`, and `bottom` to extreme values.
2. For each point `p`:
    a. Update `left` to min(`left`, p[0]).
    b. Update `right` to max(`right`, p[0]).
    c. Update `top` to max(`top`, p[1]).
    d. Update `bottom` to min(`bottom`, p[1]).
3. Return [(left + right) / 2, (top + bottom) / 2].
```

#### `distance`
```plaintext
Input: A stroke and a center point.
Output: Euclidean distance between the stroke's center and the point.

1. Compute the center of the stroke using `calculate_center`.
2. Use the Euclidean distance formula:
   sqrt((center[0] - stroke_center[0])^2 + (center[1] - stroke_center[1])^2).
```

---

## **4. Annotated Code Explanation**

```python
import numpy as np
import math
import intervals as I
PI = math.pi

def LOS(strokes):
    """
    Constructs a visibility graph from strokes using the LOS algorithm.
    
    :param strokes: List where each entry is a stroke represented by a list of 2D points.
    :return: A numpy array representing the adjacency matrix of the visibility graph.
    """
    size = len(strokes)  # Number of strokes
    edges = np.zeros((size, size))  # Initialize adjacency matrix

    for i in range(len(strokes)):
        sc = calculate_center(strokes[i])  # Center of stroke `i`
        U = I.closed(0, 2 * PI)  # Initial visibility sector (full circle)

        # Sort other strokes by their distance from `sc`
        index = sorted(range(len(strokes)), key=lambda x: distance(strokes[x], sc))

        for j in index:
            if i != j:  # Skip self
                min_theta = math.inf
                max_theta = -math.inf

                # Determine angular range subtended by stroke `j`
                for n in strokes[j]:
                    w = np.array(n) - sc  # Vector from `sc` to point `n`
                    h = np.array([1, 0])  # Horizontal reference vector

                    # Compute angle `theta`
                    if w[1] >= 0:
                        theta = math.acos(np.dot(w, h) / (np.linalg.norm(w) * np.linalg.norm(h)))
                    else:
                        theta = 2 * PI - math.acos(np.dot(w, h) / (np.linalg.norm(w) * np.linalg.norm(h)))

                    min_theta = min(min_theta, theta)
                    max_theta = max(max_theta, theta)

                h = I.closed(min_theta, max_theta)  # Angular range for stroke `j`
                V = U.intersection(h)  # Visible part

                if not V.is_empty():  # If any visibility exists
                    edges[i][j] = 1  # Add edge
                    edges[j][i] = 1
                    U = U - h  # Update visibility sector

    return edges

def calculate_center(points):
    """
    Calculates the center of a set of points by finding the midpoint of the bounding box.
    :param points: List of 2D points.
    :return: Numpy array representing the center point.
    """
    left = math.inf
    right = -math.inf
    top = -math.inf
    bottom = math.inf

    for p in points:
        left = min(left, p[0])
        right = max(right, p[0])
        top = max(top, p[1])
        bottom = min(bottom, p[1])

    return np.array([(left + right) / 2, (top + bottom) / 2])

def distance(s, center):
    """
    Calculates the Euclidean distance between two points.
    :param s: A stroke represented by a list of points.
    :param center: The center point to measure from.
    :return: Distance between the center of 's' and 'center'.
    """
    stroke_center = calculate_center(s)
    return math.sqrt((center[0] - stroke_center[0]) ** 2 + (center[1] - stroke_center[1]) ** 2)
```

---

This detailed breakdown ensures a clear understanding of the algorithm's mathematical foundation, its logical flow via pseudocode, and the precise role of each part of the implementation.