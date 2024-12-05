#include <ranges>
#include <vector>
#include <string>
#include <sstream>
#include <type_traits>

template <std::ranges::input_range R>
inline std::string join_with(R&& range, const std::string& separator) {
    using std::ranges::begin, std::ranges::end;
    std::string result;
    result.reserve(std::ranges::distance(range) * (separator.size() + 1));
    bool first = true;
    for (auto it = begin(range); it != end(range); ++it) {
        if (first) {
            first = false;
        } else {
            result.append(separator);
        }
        if constexpr (std::is_same_v<std::decay_t<decltype(*it)>, std::string>) {
            result.append(*it);
        }
        else {
            result.append(std::to_string(*it));
        }
    }
    return result;
}

template <typename T> std::vector<T> copyDropAt(const std::vector<T>& vec, size_t i) {
	std::vector<T> ret;
	ret.reserve(vec.size() - 1);
	ret.insert(ret.end(), vec.begin(), vec.begin() + i);
	ret.insert(ret.end(), vec.begin() + i + 1, vec.end());
	return ret;
}