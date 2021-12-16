from collections import Counter
from ..utils.file_utils import get_input
from ..utils.measurements import timed


@timed("Part01_dumb_version:")
def part01_slow(polymer_template: str, formulas: dict[str, str], steps: int = 10):
    element_set: set[str] = set(formulas.values())
    for step in range(steps):
        new_polymer = polymer_template[0]
        for cur_educt, next_educt in zip(polymer_template, polymer_template[1:]):
            product = formulas.get(cur_educt+next_educt)
            new_polymer += f"{'' if product is None else product}{next_educt}"
        polymer_template = new_polymer
    element_counts: set[str, int]= {k:polymer_template.count(k) for k in element_set}

    return max(element_counts.values()) - min(element_counts.values())


def polymerization_score(polymer_template: str, formulas: dict[str, str], steps: int):
    current_pairs = Counter(map(lambda t: t[0]+t[1], zip(polymer_template, polymer_template[1:])))
    product_counts = Counter(polymer_template)

    for step in range(steps):
        for (educt_a, educt_b), occurrences in current_pairs.copy().items():
            educts = educt_a+educt_b
            product = formulas[educts]
            current_pairs[educts] -= occurrences
            current_pairs[educt_a+product] += occurrences
            current_pairs[product + educt_b] += occurrences
            product_counts[product] += occurrences

    return max(product_counts.values()) - min(product_counts.values())


@timed("Part01:")
def part01_fast(polymer_template: str, formulas: dict[str, str]):
    return polymerization_score(polymer_template, formulas, 10)


@timed("Part02:")
def part02_fast(polymer_template: str, formulas: dict[str, str]):
    return polymerization_score(polymer_template, formulas, 40)


if __name__ == '__main__':
    template_input, formula_inputs = get_input().split("\n\n")
    polymer_template: str = template_input.strip()
    formulas: dict[str, str] = {educt:product for educt, product in [line.strip().split(" -> ") for line in formula_inputs.split("\n")]}

    part01_slow(polymer_template, formulas, 10)
    part01_fast(polymer_template, formulas)
    part02_fast(polymer_template, formulas)


    
