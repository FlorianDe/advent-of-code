#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <functional>
#include <ranges>
#include <sstream>
#include <string>
#include <vector>

template <typename T>
concept NumericRange = std::ranges::range<T> && std::is_arithmetic_v<std::ranges::range_value_t<T>>;

std::vector<int> stringToIntVector(const std::string& input) {
	std::istringstream stream(input);
	return std::vector<int>{std::istream_iterator<int>(stream), std::istream_iterator<int>()};
}

inline bool isSafeStep(const int delta, const int maxValueDelta) { return 1 <= delta && delta <= maxValueDelta; }

template <NumericRange Range, typename DeltaFunc>
inline bool isMonotonousSafely(Range&& range, const int maxValueDelta, DeltaFunc deltaFunc) {
	if (range.size() <= 1) {
		return true;
	}

	return std::ranges::adjacent_find(std::forward<Range>(range), [&](const int& a, const int& b) {
					 return !isSafeStep(deltaFunc(a, b), maxValueDelta);
				 }) == std::ranges::end(range);
}

template <NumericRange Range> bool isMonotonousIncreasingSafely(Range&& range, const int maxValueDelta) {
	auto deltaFuncIncreasing = [](const auto& a, const auto& b) { return b - a; };
	return isMonotonousSafely(std::forward<Range>(range), maxValueDelta, deltaFuncIncreasing);
}

template <NumericRange Range> bool isMonotonousDecreasingSafely(Range&& range, const int maxValueDelta) {
	auto deltaFuncDecreasing = [](const auto& a, const auto& b) { return a - b; };
	return isMonotonousSafely(std::forward<Range>(range), maxValueDelta, deltaFuncDecreasing);
}

template <NumericRange Range> bool isSafe(Range&& range, const int maxValueDelta) {
	return isMonotonousDecreasingSafely(std::forward<Range>(range), maxValueDelta) ||
				 isMonotonousIncreasingSafely(std::forward<Range>(range), maxValueDelta);
}

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("src/2024/inputs/day02.txt");
	auto lines = file.getLines();
	auto isSafeForDelta3 = [](auto&& range) { return isSafe(range, 3); };

	int safeCount = 0;
	int safeWithDampenerCount = 0;
	for (const auto& line : lines) {
		auto row = stringToIntVector(line);
		if (isSafeForDelta3(row)) {
			safeCount++;
		} else {
			for (int i = 0; i < row.size(); i++) {
				if (isSafeForDelta3(copyDropAt(row, i))) {
					safeWithDampenerCount++;
					break;
				}
			}
		}
	}
	auto end = std::chrono::high_resolution_clock::now();

	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (safeCount) << std::endl;
	std::cout << "Part 02: " << (safeCount + safeWithDampenerCount) << std::endl;
};