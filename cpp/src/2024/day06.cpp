#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <algorithm>
#include <chrono>
#include <string>
#include <unordered_set>
#include <utility>
#include <vector>

template <typename T> struct Vec2 {
	T y, x;

	Vec2(T y = T(0), T x = T(0)) : y(y), x(x) {}
	Vec2 operator+(const Vec2& other) const { return Vec2(y + other.y, x + other.x); }
	Vec2 operator-(const Vec2& other) const { return Vec2(y - other.y, x - other.x); }
	bool operator==(const Vec2& other) const { return y == other.y && x == other.x; }
	bool operator!=(const Vec2& other) const { return !(*this == other); }
	std::string toString() const { return std::format("({}, {})", y, x); }
};
namespace std {
template <typename T> struct hash<Vec2<T>> {
	size_t operator()(const Vec2<T>& v) const {
		size_t h1 = hash<T>{}(v.x);
		size_t h2 = hash<T>{}(v.y);
		return h1 ^ (h2 << 1);
	}
};
template <typename T1, typename T2> struct hash<std::pair<T1, T2>> {
	size_t operator()(const std::pair<T1, T2>& p) const {
		size_t h1 = std::hash<T1>{}(p.first);
		size_t h2 = std::hash<T2>{}(p.second);

		return h1 ^ (h2 << 1);
	}
};
} // namespace std

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

auto findGuardPosition(const std::string& content, size_t lineWidth) {
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
	return Vec2<std::int64_t>{row, col};
}

size_t getMapWidth(const std::string& content) {
	size_t line_break_idx = content.find('\n');
	if (line_break_idx == std::string::npos) {
		throw std::runtime_error("Couldn't determine line length!");
	}
	return line_break_idx;
}

inline auto getChar(const Vec2<int64_t>& pos, const FileData& file, const size_t width) {
	size_t index = width * pos.y + pos.x + pos.y;
	return file.content[index];
};

inline auto isInsideMap(const Vec2<int64_t>& pos, const size_t width, const size_t height) {
	return std::cmp_greater_equal(pos.x, 0) && std::cmp_less(pos.x, width) && std::cmp_greater_equal(pos.y, 0) &&
				 std::cmp_less(pos.y, height);
};

const std::array<Vec2<int64_t>, 4> DIRECTIONS = {Vec2<int64_t>{-1LL, 0LL}, Vec2<int64_t>{0LL, 1LL},
																								 Vec2<int64_t>{1LL, 0LL}, Vec2<int64_t>{0LL, -1LL}};

int part01(const FileData& file, const size_t width, const size_t height, Vec2<int64_t> pos) {
	auto dirIdx = 0; // start position looking up
	std::unordered_set<Vec2<std::int64_t>> visitedPositions{pos};

	while (isInsideMap(pos, width, height)) {
		auto nextPos = pos + DIRECTIONS[dirIdx];
		if (getChar(nextPos, file, width) == '#') {
			dirIdx = (++dirIdx) % DIRECTIONS.size();
			continue;
		}
		pos = nextPos;
		visitedPositions.insert(pos);
	}

	return visitedPositions.size() - 1;
}

int part02(const FileData& file, const size_t width, const size_t height, Vec2<int64_t> initialPos) {
	using PosType = decltype(initialPos);

	auto hasLoop = [&file, &initialPos, &width, &height](PosType addedBlockage) {
		auto dirIdx = 0; // start position looking up
		auto pos = initialPos;

		using DirectionType = decltype(dirIdx);
		std::unordered_set<std::pair<PosType, DirectionType>> visitedPositionsWithDirections;

		while (isInsideMap(pos, width, height)) {
			auto insertResult = visitedPositionsWithDirections.insert(std::make_pair(pos, dirIdx));
			if (!insertResult.second) {
				return true; // loop found!
			}
			auto nextPos = pos + DIRECTIONS[dirIdx];
			if (addedBlockage == nextPos || getChar(nextPos, file, width) == '#') {
				dirIdx = (++dirIdx) % DIRECTIONS.size();
			} else {
				pos = nextPos;
			}
		}
		return false;
	};

	auto loopCount{0L};
	for (int64_t y = 0; std::cmp_less(y, height); y++) {
		for (int64_t x = 0; std::cmp_less(x, width); x++) {
			const Vec2<int64_t> blockagePos{y, x};
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