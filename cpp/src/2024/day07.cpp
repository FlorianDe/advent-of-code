#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <array>
#include <chrono>
#include <functional>
#include <string>
#include <unordered_map>
#include <unordered_set>

struct Equation {
	int64_t result;
	std::vector<int64_t> operands;

	static Equation toEquation(const std::string& line) {
		std::istringstream iss(line);
		Equation eq;
		std::string leftPart;
		std::getline(iss, leftPart, ':');
		eq.result = std::stoll(leftPart);

		eq.operands = std::vector<int64_t>(std::istream_iterator<int64_t>(iss), std::istream_iterator<int64_t>());

		return eq;
	}
};
using OperationMap = std::unordered_map<std::string, std::function<int64_t(int64_t, int64_t)>>;

auto calculateCalibration(const std::vector<Equation>& equations, const OperationMap& operations) {
	auto isValidEquation = [&operations](const Equation& equation){
		std::unordered_set<int64_t> results{*equation.operands.begin()};
		auto operands = equation.operands;
		operands.erase(operands.begin());
		for (auto nextOperand : operands) {
			std::unordered_set<int64_t> newResults;
			newResults.reserve(results.size() * operations.size());
			for (auto prevOperand : results) {
				for (const auto& [op, func] : operations) {
					auto result = func(prevOperand, nextOperand);
					if(result <= equation.result){
						newResults.insert(result);
					}
				}
			}
			results.swap(newResults);
		}
		return results.contains(equation.result);
	};

	auto validEquationsResultSum = 0LL;
	for (const auto& equation : equations) {
		if(isValidEquation(equation)){
			validEquationsResultSum+=equation.result;
		}
	}

	return validEquationsResultSum;
}

int part02() { return 0L; }

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("../generated/aoc/inputs/2024/day07.txt");

	auto equations = file.getLines() | std::views::transform(Equation::toEquation) | std::ranges::to<std::vector>();

	OperationMap basicOperations{
			{"+", [](int64_t a, int64_t b) { return a + b; }},
			{"*", [](int64_t a, int64_t b) { return a * b; }},
	};
	OperationMap extendedOperations(basicOperations);
	extendedOperations.insert(
			{{"||", [](int64_t a, int64_t b) { return std::stoll(std::to_string(a) + std::to_string(b)); }}});

	auto part1 = calculateCalibration(equations, basicOperations);

	auto part2 = calculateCalibration(equations, extendedOperations);

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (part1) << std::endl;
	std::cout << "Part 02: " << (part2) << std::endl;
}