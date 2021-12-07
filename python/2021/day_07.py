from math import ceil, floor
from typing import Callable

from statistics import median, mean

from utils.file_utils import get_input
from utils.math_utils import Func
from utils.measurements import timed

crabs = [int(crab) for crab in get_input().strip().split(",")]


def gauss_sum(n: int) -> int:
    return (n * (n + 1)) // 2


def calculate_position_score(new_pos: int, fuel_consumption: Callable[[int], int] = Func.identity) -> int:
    return sum(fuel_consumption(abs(new_pos - crab)) for crab in crabs)


def calculate_best_position_score(fuel_consumption: Callable[[int], int] = Func.identity) -> int:
    return min(calculate_position_score(new_pos, fuel_consumption) for new_pos in range(min(crabs), max(crabs)))


@timed("Part01:")
def part01():
    return calculate_best_position_score()


@timed("Part01_Fast:")
def part01_fast():
    return calculate_position_score(int(median(crabs)))


@timed("Part02:")
def part02():
    return calculate_best_position_score(gauss_sum)


@timed("Part02_Fast:")
def part02_fast():
    mean_pos = mean(crabs)
    return min(calculate_position_score(ceil(mean_pos), gauss_sum),
               calculate_position_score(floor(mean_pos), gauss_sum))


if __name__ == '__main__':
    part01()
    part01_fast()
    part02()
    part02_fast()
