#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <format>
#include <ranges>
#include <string>
#include <string_view>
#include <vector>

inline bool isXmas(char s1, char s2, char s3, char s4) {
	return (s1 == 'X' && s2 == 'M' && s3 == 'A' && s4 == 'S') || (s1 == 'S' && s2 == 'A' && s3 == 'M' && s4 == 'X');
}

inline bool isSam(char s1, char s2, char s3) {
	return (s1 == 'S' && s2 == 'A' && s3 == 'M') || (s1 == 'M' && s2 == 'A' && s3 == 'S');
}

int part01(std::vector<std::string> lines) {
	auto const rows = std::ssize(lines);
    auto const cols = std::ssize(lines.front());

	auto xmasCount = 0L;
	// check inside rows
	for (auto y = 0; y < rows; y++) {
		for (auto x = 0; x <= cols - 4; x++) {
			if (isXmas(lines[y].at(x), lines[y].at(x + 1), lines[y].at(x + 2), lines[y].at(x + 3))) {
				xmasCount++;
			}
		}
	}
	// check cols
	for (auto x = 0; x < cols; x++) {
		for (auto y = 0; y <= rows - 4; y++) {
			if (isXmas(lines[y].at(x), lines[y + 1].at(x), lines[y + 2].at(x), lines[y + 3].at(x))) {
				xmasCount++;
			}
		}
	}
	// diagonals
	for (auto y = 0; y <= rows - 4; y++) {
		for (auto x = 0; x < cols; x++) {
			if (x <= cols - 4) {
				if (isXmas(lines[y].at(x), lines[y + 1].at(x + 1), lines[y + 2].at(x + 2),
									 lines[y + 3].at(x + 3))) {
					xmasCount++;
				}
			}
			if (x >= 3) {
				if (isXmas(lines[y].at(x), lines[y + 1].at(x - 1), lines[y + 2].at(x - 2),
									 lines[y + 3].at(x - 3))) {
					xmasCount++;
				}
			}
		}
	}
	return xmasCount;
}

int part02(std::vector<std::string> lines) {
	auto const rows = std::ssize(lines);
    auto const cols = std::ssize(lines.front());

	auto samStarCount = 0L;
	for (auto y = 1; y < rows - 1; y++) {
		for (auto x = 1; x < cols - 1; x++) {
			if (isSam(lines[y - 1].at(x - 1), lines[y].at(x), lines[y + 1].at(x + 1)) &&
					isSam(lines[y - 1].at(x + 1), lines[y].at(x), lines[y + 1].at(x - 1))) {
				samStarCount++;
			}
		}
	}

	return samStarCount;
}

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("../generated/aoc/inputs/2024/day04.txt");

	auto lines = file.getLines();

	auto part1 = part01(lines);
	auto part2 = part02(lines);

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (part1) << std::endl;
	std::cout << "Part 02: " << (part2) << std::endl;
};