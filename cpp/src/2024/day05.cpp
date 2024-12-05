#include "../utils/containers.cpp"
#include "../utils/files.cpp"
#include <algorithm>
#include <cmath>
#include <format>
#include <iostream>
#include <ranges>
#include <set>
#include <sstream>
#include <string>
#include <string_view>
#include <unordered_map>
#include <vector>

std::unordered_map<int, std::set<int>> extractOrderings(std::string_view ordersStr) {
	std::unordered_map<int, std::set<int>> order;
	std::istringstream stream{std::string(ordersStr)};
	std::string line;
	while (std::getline(stream, line)) {
		auto delimiterPos = line.find('|');
		if (delimiterPos != std::string::npos) {
			int l = std::stoi(line.substr(0, delimiterPos));
			int r = std::stoi(line.substr(delimiterPos + 1));
			order[l].insert(r);
		}
	}
	return order;
}

std::vector<std::vector<int>> extractUpdates(std::string_view updatesStr) {
	std::vector<std::vector<int>> updates;
	std::istringstream stream{std::string(updatesStr)};
	std::string line;
	while (std::getline(stream, line)) {
		std::vector<int> updateInstructions;
		for (auto&& part : line | std::ranges::views::split(std::string_view(","))) {
			std::string valueStr(part.begin(), part.end());
			updateInstructions.emplace_back(std::stoi(valueStr));
		}
		updates.push_back(updateInstructions);
	}
	return updates;
}

inline bool orderingsComparator(int a, int b, const std::unordered_map<int, std::set<int>>& orders) {
	if (orders.count(a) && orders.at(a).count(b)) {
		return true;
	}
	if (orders.count(b) && orders.at(b).count(a)) {
		return false;
	}
	return a < b;
}

int main() {
	auto start = std::chrono::high_resolution_clock::now();
	auto file = readFile("../generated/aoc/inputs/2024/day05.txt");

	auto part1 = 0L;
	auto part2 = 0L;

	auto [orders, updates] = [&]() {
		auto splitView = file.content | std::ranges::views::split(std::string_view("\n\n"));
		auto iter = splitView.begin();
		auto ordersStr = std::string((*iter).begin(), (*iter).end());
		auto updatesStr = std::string((*(++iter)).begin(), (*iter).end());
		return std::make_pair(extractOrderings(ordersStr), extractUpdates(updatesStr));
	}();

	auto orderingsComparator = [&orders](int a, int b) {
		if (orders.count(a) && orders.at(a).count(b)) {
			return true;
		}
		if (orders.count(b) && orders.at(b).count(a)) {
			return false;
		}
		return a < b;
	};

	auto getMiddleElement = [](const std::vector<int>& row) {
		auto idx = row.size() / 2;
		return row[idx];
	};

	auto fixOrdering = [orders, orderingsComparator](std::vector<int> row) {
		std::ranges::sort(row, orderingsComparator);
		return row;
	};

	for (auto&& row : updates) {
		auto valid = true;
		for (size_t i = 0; i < row.size(); i++) {
			auto curOrdering = orders[row[i]];
			for (size_t j = 0; j < i && valid; j++) {
				if (curOrdering.contains(row[j])) {
					valid = false;
					break;
				}
			}
		}
		if (valid) {
			part1 += getMiddleElement(row);
		} else {
			part2 += getMiddleElement(fixOrdering(row));
		}
	}

	auto end = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double, std::milli> duration_ms = end - start;
	std::cout << "Execution time: " << duration_ms.count() << " ms" << std::endl;
	std::cout << "Part 01: " << (part1) << std::endl;
	std::cout << "Part 02: " << (part2) << std::endl;
};