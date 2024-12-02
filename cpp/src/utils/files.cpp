#include <fstream>
#include <iostream>
#include <string>
#include <string_view>
#include <filesystem>
#include <ranges>

struct FileData {
    std::string content;
    size_t char_count;
    size_t line_count;

    auto getLines() const {
        return content | std::views::split('\n') | std::views::transform([](auto&& rng) {
            return std::string(rng.begin(), rng.end());
        });
    }
};

FileData readFile(const std::string& filename) {
    std::filesystem::path file_path(filename);
    if (!std::filesystem::exists(file_path)) {
        throw std::ios_base::failure("File does not exist: " + file_path.lexically_normal().string());
    }

    std::ifstream file(filename, std::ios::in | std::ios::ate);
    if (!file) {
        throw std::ios_base::failure("Failed to open file: " + file_path.lexically_normal().string());
    }

    std::streampos size = file.tellg();
    file.seekg(0, std::ios::beg);

    std::string buffer(size, '\0');
    file.read(&buffer[0], size);

    size_t char_count = buffer.size();
    size_t line_count = 0;
    for (char c : buffer) {
        if (c == '\n') {
            ++line_count;
        }
    }

    if (char_count > 0 && buffer[char_count - 1] != '\n') {
        ++line_count;
    }

    return {buffer, char_count, line_count};
}