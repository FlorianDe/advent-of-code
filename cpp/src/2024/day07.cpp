#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <algorithm>
#include <array>
#include <chrono>
#include <functional>
#include <numeric>
#include <string>
#include <unordered_map>
#include <unordered_set>

struct Equation {
    int64_t result;
    std::vector<int64_t> operands;

    static Equation toEquation(const std::string& line)
    {
        std::istringstream iss(line);
        std::string leftPart;
        std::getline(iss, leftPart, ':');

        return {
            std::stoll(leftPart),
            std::vector<int64_t>(std::istream_iterator<int64_t>(iss), std::istream_iterator<int64_t>())
        };
    }
};
enum class Operation { 
	Add,
    Multiply,
    Concatenate
};
using OperationMap = std::unordered_map<Operation, std::function<int64_t(int64_t, int64_t)>>;

auto calculateCalibration(const std::vector<Equation>& equations, const std::vector<Operation>&& useOps)
{
    const OperationMap operations {
        { Operation::Add, [](int64_t a, int64_t b) { return a + b; } },
        { Operation::Multiply, [](int64_t a, int64_t b) { return a * b; } },
        { Operation::Concatenate, [](int64_t a, int64_t b) { return a * std::pow<int64_t, int64_t>(10, std::log10(b) + 1) + b; } }
    };

    auto isValidEquation = [&operations, &useOps](const Equation& equation) {
        std::unordered_set<int64_t> results { *equation.operands.begin() };
        auto operands = equation.operands;
        operands.erase(operands.begin());
        for (auto nextOperand : operands) {
            std::unordered_set<int64_t> newResults;
            newResults.reserve(results.size() * operations.size());
            for (auto prevOperand : results) {
                for (const auto& op : useOps) {
                    auto result = operations.at(op)(prevOperand, nextOperand);
                    if (result <= equation.result) {
                        newResults.insert(result);
                    }
                }
            }
            results.swap(newResults);
        }
        return results.contains(equation.result);
    };

    return std::accumulate(equations.begin(), equations.end(), 0LL,
        [&isValidEquation](auto sum, const Equation& equation) {
            return isValidEquation(equation) ? sum + equation.result : sum;
        });
}

int part02() { return 0L; }

int main()
{
    auto start = std::chrono::high_resolution_clock::now();
    auto file = readFile("../generated/aoc/inputs/2024/day07.txt");

    auto equations = file.getLines() | std::views::transform(Equation::toEquation) | std::ranges::to<std::vector>();

    auto part1 = calculateCalibration(equations, { Operation::Add, Operation::Multiply });
    auto part2 = calculateCalibration(equations, { Operation::Add, Operation::Multiply, Operation::Concatenate });

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration_ms = end - start;
    std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
    std::cout << "Part 01: " << (part1) << std::endl;
    std::cout << "Part 02: " << (part2) << std::endl;
}