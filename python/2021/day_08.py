from utils.file_utils import get_input_lines
from utils.measurements import timed

digits: dict[int, str] = {
    0: 'abcefg',
    1: "cf",
    2: "acdeg",
    3: "acdfg",
    4: "bcdf",
    5: "abdfg",
    6: "abdefg",
    7: "acf",
    8: "abcdefg",
    9: "abcdfg",
}

signal_lines = [tuple(map(lambda x: x.strip().split(), signal_line.strip().split("|"))) for signal_line in
                get_input_lines()]


@timed("Part01:")
def part01():
    output_len_to_check = [len(digits[i]) for i in [1, 4, 7, 8]]
    return sum(1 for line in signal_lines for output in line[1] if len(output) in output_len_to_check)


@timed("Part02:")
def part02():
    pass


if __name__ == '__main__':
    print(signal_lines)
    part01()
    part02()
