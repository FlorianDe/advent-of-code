#include "../utils/files.cpp"
#include <algorithm>
#include <cmath>
#include <map>
#include <numeric>
#include <ranges>
#include <sstream>
#include <string>
#include <string_view>
#include <vector>

int calculateDiff(const std::pair<int, int>& pair) { return std::abs(pair.first - pair.second); }

std::map<int, int> createFrequencyMap(std::vector<int>& vec) {
	std::map<int, int> frequency_map;
	for (int v : vec) {
		frequency_map[v]++;
	}
	return frequency_map;
}

int part01(std::vector<int> left, std::vector<int> right) {
	std::sort(left.begin(), left.end());
	std::sort(right.begin(), right.end()); // reverse: std::sort(right.begin(), right.end(), std::greater<int>());

	auto diffs = std::views::zip(left, right) | std::views::transform(calculateDiff);
	auto sum = std::accumulate(diffs.begin(), diffs.end(), 0);

	return sum;
}

int part02(std::vector<int> left, std::vector<int> right) {
	auto leftFrequencies = createFrequencyMap(left);
	auto rightFrequencies = createFrequencyMap(right);

	auto sum = 0;
	for (const auto& [number, count] : leftFrequencies) {
		sum += number * count * rightFrequencies[number];
	}
	return sum;
}

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("../generated/aoc/inputs/2024/day01.txt");

	std::vector<int> left, right;
	left.reserve(file.line_count);
	right.reserve(file.line_count);

	std::istringstream stream(file.content);
	int l, r;
	while (stream >> l >> r) {
		left.push_back(l);
		right.push_back(r);
	}
	auto part1 = part01(left, right);
	auto part2 = part02(left, right);

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << part1 << std::endl;
	std::cout << part2 << std::endl;
};