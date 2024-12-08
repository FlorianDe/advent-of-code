#include "../utils/hash.cpp"
#include "../utils/files.cpp"
#include <chrono>
#include <complex>
#include <unordered_map>
#include <unordered_set>
#include <vector>
#include <utility>
#include <optional>
#include <functional>

using Pos2D = std::complex<int64_t>;
using Positions = std::vector<Pos2D>;
using SignalPositionsMap = std::unordered_map<char, Positions>;
struct SignalData {
    const size_t width;
    const size_t height;
    const SignalPositionsMap signals;
};

size_t getMapWidth(const std::string& content) {
	size_t line_break_idx = content.find('\n');
	if (line_break_idx == std::string::npos) {
		throw std::runtime_error("Couldn't determine line length!");
	}
	return line_break_idx;
}
SignalData extractSignalData(const FileData& file) {
    const auto lines = file.getLines();
    auto width = lines[0].size();
	const auto height = file.line_count;

    SignalPositionsMap signals;
    for(int64_t row = 0; std::cmp_less(row,lines.size()); row++){
        for(int64_t col = 0; std::cmp_less(col, lines[row].size()); col++){
            const auto symbol = lines[row][col];
            if(lines[row][col]!='.'){
                signals[symbol].push_back({col, row});
            }
        }
    }

	return {
        width,
        height,
        signals
    };
}

inline auto isInsideMap(const Pos2D& pos, const size_t width, const size_t height) {
	return std::cmp_greater_equal(pos.real(), 0) && std::cmp_less(pos.real(), width) &&
				 std::cmp_greater_equal(pos.imag(), 0) && std::cmp_less(pos.imag(), height);
};

auto amountOfAntinodeLocations(const SignalData& data, const std::optional<int>&& maxAntinodes){
    std::unordered_set<Pos2D, ComplexHash<int64_t>, ComplexEqual<int64_t>> uniqueAntinodeLocations;

    auto getValidAntinodes = [&data, &maxAntinodes](
        const Pos2D& startNode, 
        const Pos2D& diff,
        std::function<Pos2D(const Pos2D&, const Pos2D&, int64_t)> nodeCalculator) {

        std::vector<std::complex<int64_t>> validNodes;
        auto steps = 1LL;
        auto maxSteps = maxAntinodes.value_or(std::numeric_limits<int>::max());
        while (std::cmp_less_equal(steps, maxSteps)) {
            const auto node = nodeCalculator(startNode, diff, steps);
            if(!isInsideMap(node, data.width, data.height)){
                break;
            }
            validNodes.push_back(node);
            steps++;
        }
        return validNodes;
    };

    for(const auto& [symbol, positions]: data.signals){
        const auto includeAntennas = !maxAntinodes.has_value();
        if(includeAntennas){
            uniqueAntinodeLocations.insert_range(positions);
        }
        size_t n = positions.size();
        for (size_t i = 0; i < n - 1; ++i) {
            for (size_t j = i + 1; j < n; ++j) {
                const auto& a = positions[i];
                const auto& b = positions[j];
                const auto diff = (b-a);

                uniqueAntinodeLocations.insert_range(getValidAntinodes(a, diff, [](auto startNode, auto diff, auto steps) {
                    return startNode - steps * diff;
                }));
                uniqueAntinodeLocations.insert_range(getValidAntinodes(b, diff, [](auto startNode, auto diff, auto steps) {
                    return startNode + steps * diff;
                }));
            }
        }
    }

    return uniqueAntinodeLocations.size();
}

int main()
{   
    const auto start = std::chrono::high_resolution_clock::now();
    const auto file = readFile("../generated/aoc/inputs/2024/day08.txt");

    const SignalData data = extractSignalData(file);

    auto part1 = amountOfAntinodeLocations(data, std::make_optional(1));
    auto part2 = amountOfAntinodeLocations(data, std::nullopt);

    const auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration_ms = end - start;
    std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
    std::cout << "Part 01: " << (part1) << std::endl;
    std::cout << "Part 02: " << (part2) << std::endl;
}