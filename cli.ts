import * as fs from 'node:fs';
import * as path from 'node:path';
import * as readline from 'node:readline';
import { execSync } from 'child_process';

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

function findProjectRoot(startDir: string = __dirname): string {
    let currentDir = startDir;
    while (currentDir !== path.parse(currentDir).root) {
        const packageJsonPath = path.join(currentDir, "package.json");
        if (fs.existsSync(packageJsonPath) && fs.statSync(packageJsonPath).isFile()) {
            return currentDir;
        }
        currentDir = path.dirname(currentDir);
    }
    throw new Error("No package.json found in the directory hierarchy");
}

const __PROJECT_ROOT = findProjectRoot();
const generatedFolderPath = path.join(__PROJECT_ROOT, 'generated', 'aoc');
const aocExerciseFolderPath = path.join(generatedFolderPath, 'pages');
const aocInputsFolderPath = path.join(generatedFolderPath, 'inputs');

const padDay = (day: number) => `${day}`.padStart(2, "0")

const BASE_URL_AOC = "https://adventofcode.com";

interface ChildProcessError extends Error {
	stderr?: Buffer;
	stdout?: Buffer;
  }

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

const styles: Record<string, string> = {
	bold: '\x1b[1m', 
	reset: '\x1b[0m', 
};
const colors: Record<string, string> = {
	red: '\x1b[31m', 
	green: '\x1b[32m', 
	yellow: '\x1b[33m', 
};
const createTaggedTemplateFn = (styleCode: string, resetCode: string) => (strings: TemplateStringsArray, ...values: any[]): string => {
	let result = strings[0];
	values.forEach((value, i) => {
		result += `${styleCode}${value}${resetCode}${strings[i + 1] || ''}`;
	});
	return result;
};
export const bold = createTaggedTemplateFn(styles.bold, styles.reset);
export const red = createTaggedTemplateFn(colors.red, styles.reset);
export const green = createTaggedTemplateFn(colors.green, styles.reset);
export const yellow = createTaggedTemplateFn(colors.yellow, styles.reset);


export const sleep = (ms: number): Promise<void> => new Promise((resolve) => setTimeout(resolve, ms));

const getExerciseFileName = (year: number, day: number) => `${year}_${day}.html`;
const downloadExerciseDescription = async (year: number, day: number): Promise<string> => {
	const url = `${BASE_URL_AOC}/${year}/day/${day}`
	console.log(`Downloading day ${day} of year ${year}`)
	const response = await fetch(url, {
		"method": "GET",
		"headers": {
			"User-Agent": "https://github.com/FlorianDe/advent-of-code by FlorianDe"
		}
	});
	if(!response.ok){
		throw new Error(`Failed to fetch the aoc exercise for year: ${year}, day: ${day}. Response: ${JSON.stringify(response)}`)
	}
	const html = await response.text();

	if(!fs.existsSync(aocExerciseFolderPath)){
		fs.mkdirSync(aocExerciseFolderPath, {recursive: true})
	}
	const filePath = path.join(aocExerciseFolderPath, getExerciseFileName(year, day));
	console.log(`Writing ${year}_${day}.html file content to: ${filePath}`)
	fs.writeFileSync(filePath, html)
	await sleep(1000);

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
				const solutionPath = path.join(__PROJECT_ROOT, relativeSolutionPath)
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

const generateReadme = async () => {
	const solutions = searchSolutions();
	const readmeContent = await generateReadmeContent(solutions, config)
	fs.writeFileSync(path.join(__PROJECT_ROOT, 'README.md'), readmeContent)
}

function prompt(query: string): Promise<string> {
	return new Promise((resolve, reject) => {
	  const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
	  });
  
	  rl.question(query, (answer: string) => {
		rl.close();
		resolve(answer);
	  });
  
	  rl.on('error', (err) => {
		reject(err);
	  });
	});
  }

const isTokenValid = async (token: string): Promise<boolean> => {
	const response = await fetch(BASE_URL_AOC, {
		method: 'GET',
		headers: {
		'Cookie': `session=${token}`,
		},
	});
	return response.status < 300;
};


const getSessionTokenKeyName = (account?: string) => {
	return `aoc-session-token-${account ?? "DEFAULT_ACC"}`;
}
const promptForNewSessionToken  = async (opts?: {account?: string}) => {
	let newToken = (await prompt("Session Token:")).trim();
	while(!(await isTokenValid(newToken))){
		await prompt("Token invalid, try again.\nSession Token:")
	}
	storeSessionToken(newToken, getSessionTokenKeyName(opts?.account));
	return newToken;
}
const getSessionToken = async (opts?: {account?: string}) => {
	let token = retrieveSessionToken(getSessionTokenKeyName(opts?.account));
	if(token && await isTokenValid(token)){
		return token;
	}
	return await promptForNewSessionToken(opts);
}

function storeSessionToken(sessionToken: string, keyName: string): void {
	if (process.platform === 'darwin') {
		storeInKeychain(sessionToken, keyName);
	} else if (process.platform === 'linux') {
		storeInSecretService(sessionToken, keyName);
	} else if (process.platform === 'win32') {
		storeInWindowsVault(sessionToken, keyName);
	} else {
		throw new Error('Unsupported platform');
	}
}

function retrieveSessionToken(keyName: string): string | null {
	if (process.platform === 'darwin') {
		return getFromKeychain(keyName);
	} else if (process.platform === 'linux') {
		return getFromSecretService(keyName);
	} else if (process.platform === 'win32') {
		return getFromWindowsVault(keyName);
	} else {
		throw new Error('Unsupported platform');
	}
}

// macOS Keychain: Store and Retrieve
function storeInKeychain(token: string, keyName: string): void {
	execSync(`security add-generic-password -U -s ${keyName} -a $USER -w "${token}"`);
}
function getFromKeychain(keyName: string): string | null {
  try {
    const result = execSync(`security find-generic-password -s ${keyName} -w`);
    return result.toString().trim();
  } catch (err: unknown) {
    if (err instanceof Error && (err as ChildProcessError).stderr) {
      const childError = err as ChildProcessError;
      if (childError.stderr && childError.stderr.toString().includes('The specified item could not be found in the keychain')) {
        console.warn(yellow`Keychain item for ${keyName} not found.`);
        return null;
      }
    }
    throw new Error(`Error retrieving from keychain: ${(err as Error).message}`);
  }
}

// Linux Secret Service: Store and Retrieve
function storeInSecretService(token: string, keyName: string): void {
	execSync(`secret-tool store --label="Session Token" key ${keyName} value ${token}`);
}
function getFromSecretService(keyName: string): string | null {
	const result = execSync(`secret-tool lookup key ${keyName}`);
	return result.toString().trim();
}

// Windows Credential Vault: Store and Retrieve
function storeInWindowsVault(token: string, keyName: string): void {
	execSync(`cmdkey /add:${keyName} /user:TokenUser /pass:${token}`);
}
function getFromWindowsVault(keyName: string): string | null  {
	const result = execSync(`cmdkey /list | findstr /C:"${keyName}"`);
	return result.toString().trim();
}

export const validateYear = (year: number): void => {
	if (year < 2015) {
		throw new Error("Year must be 2015 or later.");
	  }
}
export const validateDate = (opts: {day: number, year: number}): void => {
	const {day, year} = opts;
	const currentYear = new Date().getUTCFullYear();
	if (day < 1 || day > 25) {
	  throw new Error("Day must be between 1 and 25.");
	}
	validateYear(year);
	if (year > currentYear) {
	  throw new Error(`Year cannot be in the future. Current year is ${currentYear}.`);
	}
  };
export const getCurrentYear = (): number => new Date().getUTCFullYear();
export const getCurrentDay = (year: number): number => {
	const now = new Date();
	validateYear(year);
	if (year !== now.getUTCFullYear()) {
		console.warn(yellow`Using default day 01, since none was passed.`);
		return 1;
	}
	const currentMonth = now.getUTCMonth();
	const currentDay = now.getUTCDate();
	if (currentMonth !== 11) {
		throw new Error("Advent of Code has not started yet");
	}
	if (currentDay > 25) {
		throw new Error("Advent of Code is over");
	}
	return currentDay;
};

const getSampleInputFileName = (day: number) => {
	return `day${padDay(day)}_sample.txt`
}
const getInputFileName = (day: number) => {
	return `day${padDay(day)}.txt`
}
export const getInput = async (opts: {year: number, day: number, account?: string}): Promise<void> => {
	const {
		year = new Date().getUTCFullYear(),
		day = getCurrentDay(year),
		account
	} = opts;
	validateDate({day, year});
	const aocYearInputsFolderPath =  path.join(aocInputsFolderPath, `${year}`);
	const aocInputsFilePath =  path.join(aocYearInputsFolderPath, getInputFileName(day));

	if(fs.existsSync(aocInputsFilePath) && fs.readFileSync(aocInputsFilePath, 'utf8').trim().length !== 0){
		return;
	}
	const token = await getSessionToken();
	const input = await downloadInput({day, year, token})

	if(!fs.existsSync(aocYearInputsFolderPath)){
		fs.mkdirSync(aocYearInputsFolderPath, {recursive: true})
	}
	console.log(`Writing input for ${year}/${day} to: ${aocInputsFilePath}`)
	fs.writeFileSync(aocInputsFilePath, input.trimEnd())
  };

const downloadInput = async (opts: {year: number, day: number, token: string}): Promise<string> => {
    const { year, day, token } = opts;
    const inputUrl = `/${year}/day/${day}/input`;
    const options: RequestInit = {
        method: 'GET',
        headers: {
            'Cookie': `session=${token}`
        },
    };
    const res = await fetch(`${BASE_URL_AOC}${inputUrl}`, options);
    const responseText = await res.text();

    if (res.status >= 300) {
        if (res.status === 401 || (res.status === 400 && res.headers.get('set-cookie')?.startsWith('session=;'))) {
            const newToken = await promptForNewSessionToken();
            return await downloadInput({ year, day, token: newToken });
        }
        throw new Error(`Request failed: ${res.status} ${responseText}`);
    }

    return responseText;
}

const getSampleInput = async (opts: {year: number, day: number}): Promise<void> => {
	const {
		year,
		day
	} = opts;
	validateDate({day, year});

	const aocYearInputsFolderPath =  path.join(aocInputsFolderPath, `${year}`);
	const aocSampleInputsFilePath =  path.join(aocYearInputsFolderPath, getSampleInputFileName(day));
	if(fs.existsSync(aocSampleInputsFilePath) && fs.readFileSync(aocSampleInputsFilePath, 'utf8').trim().length !== 0){
		return;
	}

	const htmlContent = await getExerciseFile(year, day);
	const regex = /<pre><code>([\s\S]*?)<\/code><\/pre>/;
	// const regex = /<pre[^>]*>\s*<code[^>]*>\s*([\s\S]*?)\s*<\/code>\s*<\/pre>/i;
	const match = htmlContent.match(regex);

	if(!match || match.length <= 1){
		throw Error(`Could not determine sample input from ${year} ${day}`);
	}
	if(!fs.existsSync(aocYearInputsFolderPath)){
		fs.mkdirSync(aocYearInputsFolderPath, {recursive: true})
	}
	console.log(`Writing sample input for ${year}/${day} to: ${aocSampleInputsFilePath}`)
	fs.writeFileSync(aocSampleInputsFilePath, match[1].trimEnd())
  };

const run = async () => {
	const [command, ...args] = process.argv.slice(2)
	switch(command){
		case "download:input":
			if(args.length != 2){
				throw new Error("When running the 'download:input' command you have to pass a year and day.")
			}
			const year = parseInt(args[0]);
			const day = parseInt(args[1]);
			await getInput({
				year,
				day
			})
			await getSampleInput({
				year,
				day
			})
			break;
		case "generate:readme": 
			await generateReadme();
			break;
		default:
			throw new Error(`The command "${command}" is not supported.`);
	}	
}
run().then()
