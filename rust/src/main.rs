use std::env;

use crate::aoc::{read_input, Solution};

mod y2015;
mod y2016;
mod aoc;

fn get_solution_fn(year: i32) -> fn(i8) -> &'static dyn Solution {
    match year {
        2015 => {y2015::days::get_solution },
        2016 => {y2016::days::get_solution },
        _ => panic!("Solutions for year {year} are not defined yet!")
    }
}

fn main() {
    let year = env::args().nth(1).unwrap().parse::<i32>().unwrap();
    let day = env::args().nth(2).unwrap().parse::<i8>().unwrap();
    let solution = get_solution_fn(year)(day);
    let part1 = solution.part01(read_input(year, day)).unwrap();
    let part2 = solution.part02(read_input(year, day)).unwrap();
    let box_size = 50;
    let div = "━".repeat(box_size);
    println!("┏{}┓", div);
    println!("┃  Year {} Day {:0>2}{: <0width$}┃", year, day, "", width=box_size-18);
    println!("┃  ┣ Part1: {: <0width$}┃", part1, width=box_size-11);
    println!("┃  ┗ Part2: {: <0width$}┃", part2, width=box_size-11);
    println!("┗{}┛", div);
}
