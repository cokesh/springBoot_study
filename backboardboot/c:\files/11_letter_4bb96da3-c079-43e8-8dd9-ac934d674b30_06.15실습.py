import math
import matplotlib.pyplot as plt
import numpy as np


mu = 100
sigma = 15
x = mu + sigma * np.random.randn(10000)
n, bins, patches = plt.hist(x, 50, density=1, facecolor='g', alpha=0.75)

plt.xlabel('sigma')
plt.ylabel('Probability')
plt.title('Histogram IQ')
plt.text(60, .025, r'$\mu=100, \ \sigma=15$')
plt.axis([40, 160, 0, 0.03])
plt.grid(True)
plt.show()
