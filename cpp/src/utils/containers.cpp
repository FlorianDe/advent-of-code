#include <vector>

template <typename T> std::vector<T> copyDropAt(const std::vector<T>& vec, size_t i) {
	std::vector<T> ret;
	ret.reserve(vec.size() - 1);
	ret.insert(ret.end(), vec.begin(), vec.begin() + i);
	ret.insert(ret.end(), vec.begin() + i + 1, vec.end());
	return ret;
}