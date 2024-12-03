#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <format>
#include <functional>
#include <regex>
#include <string>

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("src/2024/inputs/day03.txt");

	std::unordered_map<std::string, bool> shouldProcessLookup = {{"do()", true}, {"don't()", false}};
	auto shouldProcessNextMultiplications{true};
	uint64_t part1{0L};
	uint64_t part2{0L};
	std::regex pattern(std::format("{}|{}|{}", R"(mul\((\d+),(\d+)\))", R"(do\(\))", R"(don't\(\))"));
	auto regexBegin = std::sregex_iterator(file.content.begin(), file.content.end(), pattern);
	auto regexEnd = std::sregex_iterator();
	for (auto it = regexBegin; it != regexEnd; ++it) {
		const std::smatch& match = *it;
		if (shouldProcessLookup.contains(match.str(0))) {
			shouldProcessNextMultiplications = shouldProcessLookup[match.str(0)];
		} else if (match.size() > 2) {
			auto product = std::stoi(match.str(1)) * std::stoi(match.str(2));
			if (shouldProcessNextMultiplications) {
				part2 += product;
			}
			part1 += product;
		}
	}

	auto end = std::chrono::high_resolution_clock::now();

	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (part1) << std::endl;
	std::cout << "Part 02: " << (part2) << std::endl;
};