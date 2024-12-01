import * as fs from 'node:fs';
import * as path from 'node:path';

enum TemplateTokens {
	LANGUAGES_USED_PER_YEAR_TABLE = "<<LANGUAGES_USED_PER_YEAR_TABLE>>",
	YEARLY_OVERVIEW_TABLES = "<<YEARLY_OVERVIEW_TABLES>>"
}
//language=markdown
const readmeMarkdownTemplate = `
# Advent of Code

## Languages used per year
${TemplateTokens.LANGUAGES_USED_PER_YEAR_TABLE}

## Yearly Overview
${TemplateTokens.YEARLY_OVERVIEW_TABLES}
`

namespace MarkdownSymbols {
	export const ICON_RED_CROSS = ":x:"
	export const ICON_WHITE_CHECK_MARK = ":white_check_mark:"
}

type Language = string;
type Year = number;
type Day = number;
type Config = Record<Language, {
	yearResolver: (year: number) => string,
	dayResolver: (year: number, day: number) => string,
}>;

const padDay = (day: number) => `${day}`.padStart(2, "0")

const config: Config = {
	Kotlin: {
		yearResolver: (year: number) => `/kotlin/src/main/kotlin/de/florian/adventofcode/y${year}`,
		dayResolver: (year, day) => `/kotlin/src/main/kotlin/de/florian/adventofcode/y${year}/Day${padDay(day)}.kt`,
	},
	Python: {
		yearResolver: (year: number) => `/python/${year}`,
		dayResolver: (year, day) => `/python/${year}/day_${padDay(day)}.py`,
	},
	Rust: {
		yearResolver: (year: number) => `/rust/src/y${year}/days`,
		dayResolver: (year, day) => `/rust/src/y${year}/days/day${padDay(day)}.rs`,
	},
	TypeScript: {
		yearResolver: (year: number) => `/typescript/src/${year}`,
		dayResolver: (year, day) => `/typescript/src/${year}/day${padDay(day)}.ts`,
	},
	CPP: {
		yearResolver: (year: number) => `/cpp/src/${year}`,
		dayResolver: (year, day) => `/cpp/src/${year}/day${padDay(day)}.cpp`,
	}
}

const aocExerciseFolderPath = path.join(__dirname, 'generated', 'aoc');
const getExerciseFileName = (year: number, day: number) => `${year}_${day}.html`;
const downloadExerciseDescription = async (year: number, day: number): Promise<string> => {
	const url = `https://adventofcode.com/${year}/day/${day}`
	console.log(`Downloading day ${day} of year ${year}`)
	const response = await fetch(url, {
		"method": "GET"
	});
	if(!response.ok){
		throw new Error(`Failed to fetch the aoc exercise for year: ${year}, day: ${day}. Response: ${JSON.stringify(response)}`)
	}
	const html = await response.text();

	if(!fs.existsSync(aocExerciseFolderPath)){
		fs.mkdirSync(aocExerciseFolderPath, {recursive: true})
	}
	fs.writeFileSync(path.join(aocExerciseFolderPath, getExerciseFileName(year, day)), html)
	await new Promise(f => setTimeout(f, 1000));

	return html;
}

const getExerciseFile = async (year: number, day: number) => {
	const exerciseFilePath = path.join(aocExerciseFolderPath, getExerciseFileName(year, day));
	if(!fs.existsSync(exerciseFilePath)){
		return await downloadExerciseDescription(year, day);
	}
	const content = fs.readFileSync(exerciseFilePath, 'utf8');
	if(content.trim().length === 0){
		return await downloadExerciseDescription(year, day);
	}
	return content;
}

type Solutions = Record<Year, Record<Language, Record<Day, {path: string}>>>

const searchSolutions = (): Solutions => {
	const solutions: Solutions = {}
	for (const [language, langConfig] of Object.entries(config)) {
		const currentYear = new Date().getFullYear();
		for (let year = 2015; year <= currentYear; year++) {
			for (let day = 1; day <= 25; day++) {
				const relativeSolutionPath = langConfig.dayResolver(year, day);
				const solutionPath = path.join(__dirname, relativeSolutionPath)
				const solutionExists = fs.existsSync(solutionPath)
				
				if(solutionExists){
					if(!solutions[year]){
						solutions[year] = {};
					}
					if(!solutions[year][language]){
						solutions[year][language] = {}
					}
					solutions[year][language][day] = {
						path: relativeSolutionPath
					};
				}
			}
		}
	}
	return solutions;
}

const generateReadmeContent = async (solutions: Solutions, config: Config): Promise<string> => {
	const currentYear = new Date().getFullYear();
	const endTableRow = ` \n`
	const createRow = (yearColumn: string, languagesContent: string[]) => `| ${[yearColumn, ...languagesContent].join(" | ")} |`

	const generateLanguagesUsedPerYearMarkdownContent = async (): Promise<string> => {
		const getYearTableRef = (year: number): string => `[${year}](#aoc-${year})`
		const spaceYearColumn = (content: string, spacer = " ") => content.padEnd(getYearTableRef(currentYear).length, spacer)
		const getLanguageYearCheckmarkLink = (language: string, year: number) => `${MarkdownSymbols.ICON_WHITE_CHECK_MARK} ([here](${config[language].yearResolver(year)}))`
		const spaceLanguageColumn = (content: string, language: string, spacer = " ") => content.padEnd(getLanguageYearCheckmarkLink(language, currentYear).length, spacer)
		const yearHeader = spaceYearColumn(`Year `);
		const languagesHeaders = Object.keys(config).map(lang => spaceLanguageColumn(lang, lang))

		const headerRow = createRow(yearHeader, languagesHeaders);
		const dividerRow = createRow(spaceYearColumn("", "-"), Object.keys(config).map(lang => spaceLanguageColumn("", lang, "-")))
		let tableRows = ""
		for (let year = currentYear; 2015 <= year ; year--) {
			const languagesColumnContents = Object.keys(config).map(lang => solutions[year]?.[lang] ? getLanguageYearCheckmarkLink(lang,  year) : spaceLanguageColumn(MarkdownSymbols.ICON_RED_CROSS, lang))
			tableRows += createRow(getYearTableRef(year),languagesColumnContents) + endTableRow
		}
		return `${headerRow}${endTableRow}${dividerRow}${endTableRow}${tableRows}`;
	}

	const generateYearlyOverviewTable = async (year: number): Promise<string> => {
		const dayHeaderValue = 'Day'
		const getAocDayWebsiteLink = (year: number, day: number) =>  `https://adventofcode.com/${year}/day/${day}`
		const getDayColumnContent = (year: number, day: number, title: string) =>  `[${padDay(day)} - ${title}](${getAocDayWebsiteLink(year, day)})`
		const spaceDayColumn = (content: string, spacer = " ") => content.padEnd(100, spacer) //fixed size atm
		const getLanguageSolutionLink = (language: string, year: number, day: number) => `[Day ${padDay(day)} ${language}](${config[language].dayResolver(year, day)})`
		const spaceLanguageColumn = (content: string, language: string, spacer = " ") => content.padEnd(getLanguageSolutionLink(language, year, 25).length, spacer)

		const availableLangSolutions = Object.keys(config).filter(lang => !!solutions[year]?.[lang])
		const headerRow = createRow(spaceDayColumn(dayHeaderValue), availableLangSolutions.map(lang => spaceLanguageColumn(lang, lang)))
		const dividerRow = createRow(spaceDayColumn("", "-"), availableLangSolutions.map(lang => spaceLanguageColumn("", lang, "-")))

		const extractDayTitle = (description: string): string => {
			const titleRegex = /<h2>--- (?<title>.+) ---<\/h2>/;
			const { title} = titleRegex.exec(description)?.groups ?? {};
			return title ?? '-'
		}

		let tableRows = ""
		for (let day = 1; day <= 25 ; day++) {
			if(year < currentYear || (year === currentYear && day <= new Date().getDate())){ //workaround should also check time with timezone
				const aocDayDescription = await getExerciseFile(year, day);
				const dayTitle = extractDayTitle(aocDayDescription).replace(/Day.\d{0,2}:/, "").trim();
				const languagesColumnContents = availableLangSolutions.map(lang => solutions[year]?.[lang]?.[day] ? getLanguageSolutionLink(lang,  year, day) : spaceLanguageColumn("-", lang))
				tableRows += createRow(spaceDayColumn(`${getDayColumnContent(year, day, dayTitle)}`), languagesColumnContents) + endTableRow
			}
		}

		return `### AoC ${year}${endTableRow}${headerRow}${endTableRow}${dividerRow}${endTableRow}${tableRows}`;
	}

	const generateAllYearlyOverviewTables = async (): Promise<string> => {
		let content = "";
		for (let year = currentYear; 2015 <= year ; year--) {
			content += await generateYearlyOverviewTable(year) + "\n"
		}
		return content;
	}

	const languagesUsedPerYearTable = await generateLanguagesUsedPerYearMarkdownContent();
	const yearlyOverviewTable = await generateAllYearlyOverviewTables();

	return readmeMarkdownTemplate
		.replace(TemplateTokens.LANGUAGES_USED_PER_YEAR_TABLE, languagesUsedPerYearTable)
		.replace(TemplateTokens.YEARLY_OVERVIEW_TABLES, yearlyOverviewTable)
		;
}

const run = async () => {
	const solutions = searchSolutions();
	const readmeContent = await generateReadmeContent(solutions, config)
	fs.writeFileSync(path.join(__dirname, 'README.md'), readmeContent)
}
run().then()
