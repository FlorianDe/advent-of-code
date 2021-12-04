import numpy as np

input = [line.strip() for line in open("inputs/day_03.txt", "r").readlines()]
all_diagnostics = [np.array(list(map(int, list(line))), dtype='int') for line in input]


def create_sums(diagnostics):
    sums = np.zeros(len(diagnostics[0]))
    for diagnostic in diagnostics:
        sums += diagnostic
    return sums


def part01(diagnostics) -> int:
    sums = create_sums(diagnostics)

    gamma_rate_b = '0b'
    epsilon_rate_b = '0b'
    upper_bound = len(diagnostics)/2
    for elem in sums:
        if elem > upper_bound:
            gamma_rate_b += '1'
            epsilon_rate_b += '0'
        else:
            gamma_rate_b += '0'
            epsilon_rate_b += '1'

    return int(gamma_rate_b, 2) * int(epsilon_rate_b, 2)


def find_number(diagnostics, is_oxygen: bool):
    bits = len(diagnostics[0])
    filtered_values = diagnostics
    for i in range(bits):
        upper_bound = len(filtered_values)/2
        sums = create_sums(filtered_values)
        filter_value = int(sums[i] >= upper_bound if is_oxygen else sums[i] < upper_bound)
        filtered_values = list(filter(lambda diag: diag[i] == filter_value, filtered_values))
        if len(filtered_values) == 1:
            break

    return filtered_values[0]


def part02(diagnostics) -> int:
    oxygen_generator_rating = find_number(diagnostics, True)
    co2_scrubber_rating = find_number(diagnostics, False)

    return int(''.join(list(map(str, oxygen_generator_rating))), 2) * int(''.join(list(map(str, co2_scrubber_rating))), 2)


if __name__ == '__main__':
    print(f"Part01: {part01(all_diagnostics)}")
    print(f"Part02: {part02(all_diagnostics)}")
