from functools import cache

from itertools import groupby

from utils.file_utils import get_input
from utils.measurements import timed


input = get_input().strip()
lanterns = [int(counter) for counter in input.strip().split(",")]


def estimate_fish_population(fish: list[int], days: int = 80, offsprings: list[int] = (7, 9)) -> int:
    fish_groups = {k: len(list(v)) for k, v in groupby(sorted(fish))}

    @cache
    def count_siblings(days_left: int, amount: int, days: int):
        if days_left <= days:
            return sum(count_siblings(c, amount, days - days_left) for c in offsprings)
        return amount

    return sum(count_siblings(days_left + 1, amount, days) for days_left, amount in fish_groups.items())


@timed("Part01:")
def part01():
    return estimate_fish_population(lanterns)


@timed("Part02:")
def part02():
    return estimate_fish_population(lanterns, days=256)


@timed("Part02_Fast:")
def part02_fast(days: int = 256):
    groups = [input.count(str(i)) for i in range(9)]
    for _ in range(days):
        births = groups[0]
        groups = groups[1:] + [births]
        groups[6] += births
    return sum(groups)


if __name__ == '__main__':
    part01()
    part02()
    part02_fast(256)
