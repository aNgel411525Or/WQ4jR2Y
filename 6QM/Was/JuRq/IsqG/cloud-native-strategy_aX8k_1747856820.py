from scipy.stats import ttest_ind

# 95th percentile response times (ms)
lift_shift_rts = [1200, 950, 800, 1100, 1340]

t_stat, p = ttest_ind(lift_shift_rts, cloud_native_rts, equal_var=False)
print(f"T-statistic: {t_stat:.3f}")
