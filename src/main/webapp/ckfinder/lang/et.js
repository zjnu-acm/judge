/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object for the Estonian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['et'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, pole saadaval</span>',
		confirmCancel	: 'M\xF5ned valikud on muudetud. Kas oled kindel, et tahad dialoogiakna sulgeda?',
		ok				: 'Olgu',
		cancel			: 'Loobu',
		confirmationTitle	: 'Kinnitus',
		messageTitle	: 'Andmed',
		inputTitle		: 'K\xFCsimus',
		undo			: 'V\xF5ta tagasi',
		redo			: 'Tee uuesti',
		skip			: 'J\xE4ta vahele',
		skipAll			: 'J\xE4ta k\xF5ik vahele',
		makeDecision	: 'Mida tuleks teha?',
		rememberDecision: 'J\xE4ta valik meelde'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'et',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'yyyy-mm-dd H:MM',
	DateAmPm : ['EL', 'PL'],

	// Folders
	FoldersTitle	: 'Kaustad',
	FolderLoading	: 'Laadimine...',
	FolderNew		: 'Palun sisesta uue kataloogi nimi: ',
	FolderRename	: 'Palun sisesta uue kataloogi nimi: ',
	FolderDelete	: 'Kas tahad kindlasti kausta "%1" kustutada?',
	FolderRenaming	: ' (\xFCmbernimetamine...)',
	FolderDeleting	: ' (kustutamine...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Palun sisesta faili uus nimi: ',
	FileRenameExt	: 'Kas oled kindel, et tahad faili laiendit muuta? Fail v\xF5ib muutuda kasutamatuks.',
	FileRenaming	: '\xDCmbernimetamine...',
	FileDelete		: 'Kas oled kindel, et tahad kustutada faili "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laadimine...',
	FilesEmpty		: 'See kaust on t\xFChi.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Korv',
	BasketClear			: 'T\xFChjenda korv',
	BasketRemove		: 'Eemalda korvist',
	BasketOpenFolder	: 'Ava \xFClemine kaust',
	BasketTruncateConfirm : 'Kas tahad t\xF5esti eemaldada korvist k\xF5ik failid?',
	BasketRemoveConfirm	: 'Kas tahad t\xF5esti eemaldada korvist faili "%1"?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Korvis ei ole \xFChtegi faili, lohista m\xF5ni siia.',
	BasketCopyFilesHere	: 'Failide kopeerimine korvist',
	BasketMoveFilesHere	: 'Failide liigutamine korvist',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Laadi \xFCles',
	UploadTip	: 'Laadi \xFCles uus fail',
	Refresh		: 'V\xE4rskenda',
	Settings	: 'S\xE4tted',
	Help		: 'Abi',
	HelpTip		: 'Abi',

	// Context Menus
	Select			: 'Vali',
	SelectThumbnail : 'Vali pisipilt',
	View			: 'Kuva',
	Download		: 'Laadi alla',

	NewSubFolder	: 'Uus alamkaust',
	Rename			: 'Nimeta \xFCmber',
	Delete			: 'Kustuta',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopeeri siia',
	MoveDragDrop	: 'Liiguta siia',

	// Dialogs
	RenameDlgTitle		: '\xDCmbernimetamine',
	NewNameDlgTitle		: 'Uue nime andmine',
	FileExistsDlgTitle	: 'Fail on juba olemas',
	SysErrorDlgTitle : 'S\xFCsteemi viga',

	FileOverwrite	: 'Kirjuta \xFCle',
	FileAutorename	: 'Nimeta automaatselt \xFCmber',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Olgu',
	CancelBtn	: 'Loobu',
	CloseBtn	: 'Sulge',

	// Upload Panel
	UploadTitle			: 'Uue faili \xFCleslaadimine',
	UploadSelectLbl		: 'Vali \xFCleslaadimiseks fail',
	UploadProgressLbl	: '(\xDCleslaadimine, palun oota...)',
	UploadBtn			: 'Laadi valitud fail \xFCles',
	UploadBtnCancel		: 'Loobu',

	UploadNoFileMsg		: 'Palun vali fail oma arvutist.',
	UploadNoFolder		: 'Palun vali enne \xFCleslaadimist kataloog.',
	UploadNoPerms		: 'Failide \xFCleslaadimine pole lubatud.',
	UploadUnknError		: 'Viga faili saatmisel.',
	UploadExtIncorrect	: 'Selline faili laiend pole selles kaustas lubatud.',

	// Flash Uploads
	UploadLabel			: '\xDCleslaaditavad failid',
	UploadTotalFiles	: 'Faile kokku:',
	UploadTotalSize		: 'Kogusuurus:',
	UploadSend			: 'Laadi \xFCles',
	UploadAddFiles		: 'Lisa faile',
	UploadClearFiles	: 'Eemalda failid',
	UploadCancel		: 'Katkesta \xFCleslaadimine',
	UploadRemove		: 'Eemalda',
	UploadRemoveTip		: 'Eemalda !f',
	UploadUploaded		: '!n% \xFCles laaditud',
	UploadProcessing	: 'T\xF6\xF6tlemine...',

	// Settings Panel
	SetTitle		: 'S\xE4tted',
	SetView			: 'Vaade:',
	SetViewThumb	: 'Pisipildid',
	SetViewList		: 'Loend',
	SetDisplay		: 'Kuva:',
	SetDisplayName	: 'Faili nimi',
	SetDisplayDate	: 'Kuup\xE4ev',
	SetDisplaySize	: 'Faili suurus',
	SetSort			: 'Sortimine:',
	SetSortName		: 'faili nime j\xE4rgi',
	SetSortDate		: 'kuup\xE4eva j\xE4rgi',
	SetSortSize		: 'suuruse j\xE4rgi',
	SetSortExtension		: 'laiendi j\xE4rgi',

	// Status Bar
	FilesCountEmpty : '<t\xFChi kaust>',
	FilesCountOne	: '1 fail',
	FilesCountMany	: '%1 faili',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'P\xE4ringu t\xE4itmine ei olnud v\xF5imalik. (Viga %1)',
	Errors :
	{
	 10 : 'Vigane k\xE4sk.',
	 11 : 'Allika liik ei olnud p\xE4ringus m\xE4\xE4ratud.',
	 12 : 'P\xE4ritud liik ei ole sobiv.',
	102 : 'Sobimatu faili v\xF5i kausta nimi.',
	103 : 'Piiratud \xF5iguste t\xF5ttu ei olnud v\xF5imalik p\xE4ringut l\xF5petada.',
	104 : 'Failis\xFCsteemi piiratud \xF5iguste t\xF5ttu ei olnud v\xF5imalik p\xE4ringut l\xF5petada.',
	105 : 'Sobimatu faililaiend.',
	109 : 'Vigane p\xE4ring.',
	110 : 'Tundmatu viga.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Sellenimeline fail v\xF5i kaust on juba olemas.',
	116 : 'Kausta ei leitud. Palun v\xE4rskenda lehte ja proovi uuesti.',
	117 : 'Faili ei leitud. Palun v\xE4rskenda lehte ja proovi uuesti.',
	118 : 'L\xE4hte- ja sihtasukoht on sama.',
	201 : 'Samanimeline fail on juba olemas. \xDCles laaditud faili nimeks pandi "%1".',
	202 : 'Vigane fail.',
	203 : 'Vigane fail. Fail on liiga suur.',
	204 : '\xDCleslaaditud fail on rikutud.',
	205 : 'Serverisse \xFCleslaadimiseks pole \xFChtegi ajutiste failide kataloogi.',
	206 : '\xDCleslaadimine katkestati turvakaalutlustel. Fail sisaldab HTMLi sarnaseid andmeid.',
	207 : '\xDCleslaaditud faili nimeks pandi "%1".',
	300 : 'Faili(de) liigutamine nurjus.',
	301 : 'Faili(de) kopeerimine nurjus.',
	500 : 'Failide sirvija on turvakaalutlustel keelatud. Palun v\xF5ta \xFChendust oma s\xFCsteemi administraatoriga ja kontrolli CKFinderi seadistusfaili.',
	501 : 'Pisipiltide tugi on keelatud.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Faili nimi ei tohi olla t\xFChi.',
		FileExists		: 'Fail nimega %s on juba olemas.',
		FolderEmpty		: 'Kausta nimi ei tohi olla t\xFChi.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Faili nimi ei tohi sisaldada \xFChtegi j\xE4rgnevatest m\xE4rkidest: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Faili nimi ei tohi sisaldada \xFChtegi j\xE4rgnevatest m\xE4rkidest: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Faili avamine uues aknas polnud v\xF5imalik. Palun seadista oma brauserit ning keela k\xF5ik h\xFCpikakende blokeerijad selle saidi jaoks.',
		XmlError		: 'XML vastust veebiserverist polnud v\xF5imalik korrektselt laadida.',
		XmlEmpty		: 'XML vastust veebiserverist polnud v\xF5imalik korrektselt laadida. Serveri vastus oli t\xFChi.',
		XmlRawResponse	: 'Serveri vastus toorkujul: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: '%s suuruse muutmine',
		sizeTooBig		: 'Pildi k\xF5rgust ega laiust ei saa m\xE4\xE4rata suuremaks pildi esialgsest vastavast m\xF5\xF5tmest (%size).',
		resizeSuccess	: 'Pildi suuruse muutmine \xF5nnestus.',
		thumbnailNew	: 'Tee uus pisipilt',
		thumbnailSmall	: 'V\xE4ike (%s)',
		thumbnailMedium	: 'Keskmine (%s)',
		thumbnailLarge	: 'Suur (%s)',
		newSize			: 'M\xE4\xE4ra uus suurus',
		width			: 'Laius',
		height			: 'K\xF5rgus',
		invalidHeight	: 'Sobimatu k\xF5rgus.',
		invalidWidth	: 'Sobimatu laius.',
		invalidName		: 'Sobimatu faili nimi.',
		newImage		: 'Loo uus pilt',
		noExtensionChange : 'Faili laiendit pole v\xF5imalik muuta.',
		imageSmall		: 'L\xE4htepilt on liiga v\xE4ike.',
		contextMenuName	: 'Muuda suurust',
		lockRatio		: 'Lukusta k\xFClgede suhe',
		resetSize		: 'L\xE4htesta suurus'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Salvesta',
		fileOpenError	: 'Faili avamine pole v\xF5imalik.',
		fileSaveSuccess	: 'Faili salvestamine \xF5nnestus.',
		contextMenuName	: 'Muuda',
		loadingFile		: 'Faili laadimine, palun oota...'
	},

	Maximize :
	{
		maximize : 'Maksimeeri',
		minimize : 'Minimeeri'
	},

	Gallery :
	{
		current : 'Pilt {current}, kokku {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Extract here', // MISSING
		extractToLabel		: 'Extract to...', // MISSING
		downloadZipLabel	: 'Download as zip', // MISSING
		compressZipLabel	: 'Compress to zip', // MISSING
		removeAndExtract	: 'Remove existing and extract', // MISSING
		extractAndOverwrite	: 'Extract overwriting existing files', // MISSING
		extractSuccess		: 'File extracted successfully.' // MISSING
	},

	Search :
	{
		searchPlaceholder : 'Otsimine'
	}
};
