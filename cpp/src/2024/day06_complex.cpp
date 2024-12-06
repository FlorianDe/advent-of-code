#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <algorithm>
#include <chrono>
#include <complex>
#include <string>
#include <unordered_set>
#include <utility>
#include <vector>

namespace std {
template <typename T> struct std::hash<std::complex<T>> {
	size_t operator()(const std::complex<T>& c) const {
		size_t h1 = std::hash<T>{}(c.real());
		size_t h2 = std::hash<T>{}(c.imag());
		return h1 ^ (h2 << 1);
	}
};
template <typename T, typename U> struct std::hash<std::pair<std::complex<T>, U>> {
	size_t operator()(const std::pair<std::complex<T>, U>& p) const {
		size_t h1 = std::hash<std::complex<T>>{}(p.first);
		size_t h2 = std::hash<U>{}(p.second);
		return h1 ^ (h2 << 1);
	}
};
} // namespace std
const std::complex<int64_t> COMPLEX_CLOCKWISE_ROTATION{0, -1};

std::vector<std::vector<int>> extractUpdates(std::string_view updatesStr) {
	std::vector<std::vector<int>> updates;
	std::istringstream stream{std::string(updatesStr)};
	std::string line;
	while (std::getline(stream, line)) {
		std::vector<int> updateInstructions;
		for (auto&& part : line | std::ranges::views::split(',')) {
			updateInstructions.emplace_back(std::stoi(std::string(part.begin(), part.end())));
		}
		updates.push_back(updateInstructions);
	}
	return updates;
}

std::complex<std::int64_t> findGuardPosition(const std::string& content, size_t lineWidth) {
	size_t guard_idx = content.find('^');
	if (guard_idx == std::string::npos) {
		throw std::runtime_error("Couldn't determine line length!");
	}

	size_t adjustedLineWidth = lineWidth + 1;
	std::int64_t row = static_cast<std::int64_t>(guard_idx / adjustedLineWidth);
	std::int64_t col = static_cast<std::int64_t>(guard_idx % adjustedLineWidth);
	if (std::cmp_equal(col, adjustedLineWidth - 1)) {
		col = 0;
		row--;
	}
	return {row, col};
}

size_t getMapWidth(const std::string& content) {
	size_t line_break_idx = content.find('\n');
	if (line_break_idx == std::string::npos) {
		throw std::runtime_error("Couldn't determine line length!");
	}
	return line_break_idx;
}

inline auto getChar(const std::complex<int64_t>& pos, const FileData& file, const size_t width) {
	size_t index = width * pos.real() + pos.imag() + pos.real();
	return file.content[index];
};

inline auto isInsideMap(const std::complex<int64_t>& pos, const size_t width, const size_t height) {
	return std::cmp_greater_equal(pos.real(), 0) && std::cmp_less(pos.real(), width) &&
				 std::cmp_greater_equal(pos.imag(), 0) && std::cmp_less(pos.imag(), height);
};

int part01(const FileData& file, const size_t width, const size_t height, std::complex<int64_t> pos) {
	auto dir = std::complex<int64_t>(-1, 0); // start position looking up
	std::unordered_set<std::complex<std::int64_t>> visitedPositions{pos};

	while (isInsideMap(pos, width, height)) {
		auto nextPos = pos + dir;
		if (getChar(nextPos, file, width) == '#') {
			dir *= COMPLEX_CLOCKWISE_ROTATION;
			continue;
		}
		pos = nextPos;
		visitedPositions.insert(pos);
	}

	return visitedPositions.size() - 1;
}

int part02(const FileData& file, const size_t width, const size_t height, std::complex<int64_t> initialPos) {
	using PosType = decltype(initialPos);

	auto hasLoop = [&file, &initialPos, &width, &height](PosType addedBlockage) {
		auto dir = std::complex<int64_t>(-1, 0); // start position looking up
		auto pos = initialPos;

		using DirectionType = decltype(dir);
		std::unordered_set<std::pair<PosType, DirectionType>> visitedPositionsWithDirections;

		while (isInsideMap(pos, width, height)) {
			auto insertResult = visitedPositionsWithDirections.insert(std::make_pair(pos, dir));
			if (!insertResult.second) {
				return true; // loop found!
			}
			auto nextPos = pos + dir;
			if (addedBlockage == nextPos || getChar(nextPos, file, width) == '#') {
				dir *= COMPLEX_CLOCKWISE_ROTATION;
			} else {
				pos = nextPos;
			}
		}
		return false;
	};

	auto loopCount{0L};
	for (int64_t y = 0; std::cmp_less(y, height); y++) {
		for (int64_t x = 0; std::cmp_less(x, width); x++) {
			const std::complex<int64_t> blockagePos{y, x};
			if (getChar(blockagePos, file, width) != '#' && blockagePos != initialPos && hasLoop(blockagePos)) {
				loopCount++;
			}
		}
	}

	return loopCount;
}

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("../generated/aoc/inputs/2024/day06.txt");

	const auto width = getMapWidth(file.content);
	const auto height = file.line_count;
	const auto initialPos = findGuardPosition(file.content, width);

	auto part1 = part01(file, width, height, initialPos);
	auto part2 = part02(file, width, height, initialPos);

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (part1) << std::endl;
	std::cout << "Part 02: " << (part2) << std::endl;
}