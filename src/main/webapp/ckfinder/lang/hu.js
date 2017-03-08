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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Hungarian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['hu'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nem el\xE9rhet\u0151</span>',
		confirmCancel	: 'Az \u0171rlap tartalma megv\xE1ltozott, \xE1m a v\xE1ltoz\xE1sokat nem r\xF6gz\xEDtette. Biztosan be szeretn\xE9 z\xE1rni az \u0171rlapot?',
		ok				: 'Rendben',
		cancel			: 'M\xE9gsem',
		confirmationTitle	: 'Meger\u0151s\xEDt\xE9s',
		messageTitle	: 'Inform\xE1ci\xF3',
		inputTitle		: 'K\xE9rd\xE9s',
		undo			: 'Visszavon\xE1s',
		redo			: 'Ism\xE9tl\xE9s',
		skip			: 'Kihagy',
		skipAll			: 'Mindet kihagy',
		makeDecision	: 'Mi t\xF6rt\xE9njen a f\xE1jllal?',
		rememberDecision: 'Jegyezze meg a v\xE1laszt\xE1somat'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'hu',

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
	DateTime : 'yyyy.mm.dd. HH:MM',
	DateAmPm : ['de.', 'du.'],

	// Folders
	FoldersTitle	: 'Mapp\xE1k',
	FolderLoading	: 'Bet\xF6lt\xE9s...',
	FolderNew		: 'K\xE9rem adja meg a mappa nev\xE9t: ',
	FolderRename	: 'K\xE9rem adja meg a mappa \xFAj nev\xE9t: ',
	FolderDelete	: 'Biztosan t\xF6r\xF6lni szeretn\xE9 a k\xF6vetkez\u0151 mapp\xE1t: "%1"?',
	FolderRenaming	: ' (\xE1tnevez\xE9s...)',
	FolderDeleting	: ' (t\xF6rl\xE9s...)',
	DestinationFolder	: 'C\xE9l mappa',

	// Files
	FileRename		: 'K\xE9rem adja meg a f\xE1jl \xFAj nev\xE9t: ',
	FileRenameExt	: 'Biztosan szeretn\xE9 m\xF3dos\xEDtani a f\xE1jl kiterjeszt\xE9s\xE9t? A f\xE1jl esetleg haszn\xE1lhatatlan lesz.',
	FileRenaming	: '\xC1tnevez\xE9s...',
	FileDelete		: 'Biztosan t\xF6rli a k\xF6vetkez\u0151 f\xE1jlt: "%1"?',
	FilesDelete	: 'Biztosan t\xF6rli a kijel\xF6lt %1 f\xE1jlt?',
	FilesLoading	: 'Bet\xF6lt\xE9s...',
	FilesEmpty		: 'A mappa \xFCres.',
	DestinationFile	: 'C\xE9l f\xE1jl',
	SkippedFiles	: 'A kihagyott f\xE1jlok list\xE1ja:',

	// Basket
	BasketFolder		: 'Kos\xE1r',
	BasketClear			: 'Kos\xE1r \xFCr\xEDt\xE9se',
	BasketRemove		: 'T\xF6rl\xE9s a kos\xE1rb\xF3l',
	BasketOpenFolder	: 'A f\xE1jlt tartalmaz\xF3 mappa megnyit\xE1sa',
	BasketTruncateConfirm : 'Biztosan szeretne minden f\xE1jlt t\xF6r\xF6lni a kos\xE1rb\xF3l?',
	BasketRemoveConfirm	: 'Biztosan t\xF6r\xF6lni szeretn\xE9 a(z) "%1" nev\u0171 f\xE1jlt a kos\xE1rb\xF3l?',
	BasketRemoveConfirmMultiple	: 'Biztosan t\xF6r\xF6lni szeretn\xE9 a kijel\xFClt %1 f\xE1jlt a kos\xE1rb\xF3l?',
	BasketEmpty			: 'Nincsenek f\xE1jlok a kos\xE1rban.',
	BasketCopyFilesHere	: 'F\xE1jlok m\xE1sol\xE1sa a kos\xE1rb\xF3l',
	BasketMoveFilesHere	: 'F\xE1jlok \xE1thelyez\xE9se a kos\xE1rb\xF3l',

	// Global messages
	OperationCompletedSuccess	: 'A m\u0171velet sikeresen befejez\u0151d\xF6tt.',
	OperationCompletedErrors		: 'A m\u0171velet k\xF6zben hiba t\xF6rt\xE9nt.',
	FileError				: '%s: %e',

	// Move and Copy files
	MovedFilesNumber		: 'Az \xE1thelyezett f\xE1jlok sz\xE1ma: %s.',
	CopiedFilesNumber	: 'A m\xE1solt f\xE1jlok sz\xE1ma: %s.',
	MoveFailedList		: 'A k\xF6vetkez\u0151 f\xE1jlok nem helyezhet\u0151ek \xE1t:<br />%s',
	CopyFailedList		: 'A k\xF6vetkez\u0151 f\xE1jlok nem m\xE1solhat\xF3ak:<br />%s',

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Felt\xF6lt\xE9s',
	UploadTip	: '\xDAj f\xE1jl felt\xF6lt\xE9se',
	Refresh		: 'Friss\xEDt\xE9s',
	Settings	: 'Be\xE1ll\xEDt\xE1sok',
	Help		: 'S\xFAg\xF3',
	HelpTip		: 'S\xFAg\xF3 (angolul)',

	// Context Menus
	Select			: 'Kiv\xE1laszt',
	SelectThumbnail : 'B\xE9lyegk\xE9p kiv\xE1laszt\xE1sa',
	View			: 'Megtekint\xE9s',
	Download		: 'Let\xF6lt\xE9s',

	NewSubFolder	: '\xDAj almappa',
	Rename			: '\xC1tnevez\xE9s',
	Delete			: 'T\xF6rl\xE9s',
	DeleteFiles		: 'F\xE1jlok t\xF6rl\xE9se',

	CopyDragDrop	: 'M\xE1sol\xE1s ide',
	MoveDragDrop	: '\xC1thelyez\xE9s ide',

	// Dialogs
	RenameDlgTitle		: '\xC1tnevez\xE9s',
	NewNameDlgTitle		: '\xDAj n\xE9v',
	FileExistsDlgTitle	: 'A f\xE1jl m\xE1r l\xE9tezik',
	SysErrorDlgTitle : 'Rendszerhiba',

	FileOverwrite	: 'Fel\xFCl\xEDr',
	FileAutorename	: 'Automatikus \xE1tnevez\xE9s',
	ManuallyRename	: '\xC1tnevez\xE9s',

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'M\xE9gsem',
	CloseBtn	: 'Bez\xE1r\xE1s',

	// Upload Panel
	UploadTitle			: '\xDAj f\xE1jl felt\xF6lt\xE9se',
	UploadSelectLbl		: 'V\xE1lassza ki a felt\xF6lteni k\xEDv\xE1nt f\xE1jlt',
	UploadProgressLbl	: '(A felt\xF6lt\xE9s folyamatban, k\xE9rem v\xE1rjon...)',
	UploadBtn			: 'A kiv\xE1lasztott f\xE1jl felt\xF6lt\xE9se',
	UploadBtnCancel		: 'M\xE9gsem',

	UploadNoFileMsg		: 'K\xE9rem v\xE1lassza ki a f\xE1jlt a sz\xE1m\xEDt\xF3g\xE9p\xE9r\u0151l.',
	UploadNoFolder		: 'A felt\xF6lt\xE9s el\u0151tt v\xE1lasszon mapp\xE1t.',
	UploadNoPerms		: 'A f\xE1jlok felt\xF6lt\xE9se nem enged\xE9lyezett.',
	UploadUnknError		: 'Hiba a f\xE1jl felt\xF6lt\xE9se k\xF6zben.',
	UploadExtIncorrect	: 'A f\xE1jl kiterjeszt\xE9se nem enged\xE9lyezett ebben a mapp\xE1ban.',

	// Flash Uploads
	UploadLabel			: 'Felt\xF6ltend\u0151 f\xE1jlok',
	UploadTotalFiles	: '\xD6sszes f\xE1jl:',
	UploadTotalSize		: '\xD6sszm\xE9ret:',
	UploadSend			: 'Felt\xF6lt\xE9s',
	UploadAddFiles		: 'F\xE1jl hozz\xE1ad\xE1sa',
	UploadClearFiles	: 'Felt\xF6lt\xE9si lista t\xF6rl\xE9se',
	UploadCancel		: 'Felt\xF6lt\xE9s megszak\xEDt\xE1sa',
	UploadRemove		: 'Elt\xE1vol\xEDt',
	UploadRemoveTip		: 'F\xE1jl elt\xE1vol\xEDt\xE1sa a list\xE1r\xF3l: !f',
	UploadUploaded		: 'Felt\xF6ltve !n%',
	UploadProcessing	: 'Feldolgoz\xE1s...',

	// Settings Panel
	SetTitle		: 'Be\xE1ll\xEDt\xE1sok',
	SetView			: 'N\xE9zet:',
	SetViewThumb	: 'b\xE9lyegk\xE9pes',
	SetViewList		: 'list\xE1s',
	SetDisplay		: 'Megjelenik:',
	SetDisplayName	: 'f\xE1jl neve',
	SetDisplayDate	: 'd\xE1tum',
	SetDisplaySize	: 'f\xE1jlm\xE9ret',
	SetSort			: 'Rendez\xE9s:',
	SetSortName		: 'f\xE1jln\xE9v',
	SetSortDate		: 'd\xE1tum',
	SetSortSize		: 'm\xE9ret',
	SetSortExtension		: 'kiterjeszt\xE9s',

	// Status Bar
	FilesCountEmpty : '<\xFCres mappa>',
	FilesCountOne	: '1 f\xE1jl',
	FilesCountMany	: '%1 f\xE1jl',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'A parancsot nem siker\xFClt v\xE9grehajtani. (Hiba: %1)',
	Errors :
	{
	 10 : '\xC9rv\xE9nytelen parancs.',
	 11 : 'A f\xE1jl t\xEDpusa nem lett a k\xE9r\xE9s sor\xE1n be\xE1ll\xEDtva.',
	 12 : 'A k\xEDv\xE1nt f\xE1jl t\xEDpus \xE9rv\xE9nytelen.',
	102 : '\xC9rv\xE9nytelen f\xE1jl vagy k\xF6nyvt\xE1rn\xE9v.',
	103 : 'Hiteles\xEDt\xE9si probl\xE9m\xE1k miatt nem siker\xFClt a k\xE9r\xE9st teljes\xEDteni.',
	104 : 'Jogosults\xE1gi probl\xE9m\xE1k miatt nem siker\xFClt a k\xE9r\xE9st teljes\xEDteni.',
	105 : '\xC9rv\xE9nytelen f\xE1jl kiterjeszt\xE9s.',
	109 : '\xC9rv\xE9nytelen k\xE9r\xE9s.',
	110 : 'Ismeretlen hiba.',
	111 : 'A k\xE9r\xE9s nem teljes\xEDthet\u0151 a l\xE9trej\xF6v\u0151 f\xE1jl m\xE9rete miatt.',
	115 : 'A f\xE1lj vagy mappa m\xE1r l\xE9tezik ezen a n\xE9ven.',
	116 : 'Mappa nem tal\xE1lhat\xF3. K\xE9rem friss\xEDtsen \xE9s pr\xF3b\xE1lja \xFAjra.',
	117 : 'F\xE1jl nem tal\xE1lhat\xF3. K\xE9rem friss\xEDtsen \xE9s pr\xF3b\xE1lja \xFAjra.',
	118 : 'A forr\xE1s \xE9s a c\xE9l azonos.',
	201 : 'Ilyen nev\u0171 f\xE1jl m\xE1r l\xE9tezett. A felt\xF6lt\xF6tt f\xE1jl a k\xF6vetkez\u0151re lett \xE1tnevezve: "%1".',
	202 : '\xC9rv\xE9nytelen f\xE1jl.',
	203 : '\xC9rv\xE9nytelen f\xE1jl. A f\xE1jl m\xE9rete t\xFAl nagy.',
	204 : 'A felt\xF6lt\xF6tt f\xE1jl hib\xE1s.',
	205 : 'A szerveren nem tal\xE1lhat\xF3 a felt\xF6lt\xE9shez ideiglenes mappa.',
	206 : 'A f\xE1jl felt\xF6t\xE9se biztons\xE1gi okb\xF3l megszakadt. A f\xE1jl HTML adatokat tartalmaz.',
	207 : 'El fichero subido ha sido renombrado como "%1".',
	300 : 'A f\xE1jl(ok) \xE1thelyez\xE9se sikertelen.',
	301 : 'A f\xE1jl(ok) m\xE1sol\xE1sa sikertelen.',
	500 : 'A f\xE1jl-tall\xF3z\xF3 biztons\xE1gi okok miatt nincs enged\xE9lyezve. K\xE9rem vegye fel a kapcsolatot a rendszer \xFCzemeltet\u0151j\xE9vel \xE9s ellen\u0151rizze a CKFinder konfigur\xE1ci\xF3s f\xE1jlt.',
	501 : 'A b\xE9lyegk\xE9p t\xE1mogat\xE1s nincs enged\xE9lyezve.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'A f\xE1jl neve nem lehet \xFCres.',
		FileExists		: 'A(z) %s f\xE1jl m\xE1r l\xE9tezik.',
		FolderEmpty		: 'A mappa neve nem lehet \xFCres.',
		FolderExists	: 'A(z) %s mappa m\xE1r l\xE9tezik.',
		FolderNameExists	: 'A mappa l\xE9tezik.',

		FileInvChar		: 'A f\xE1jl neve nem tartalmazhatja a k\xF6vetkez\u0151 karaktereket: \n\\ / : * ? " < > |',
		FolderInvChar	: 'A mappa neve nem tartalmazhatja a k\xF6vetkez\u0151 karaktereket: \n\\ / : * ? " < > |',

		PopupBlockView	: 'A felugr\xF3 ablak megnyit\xE1sa nem siker\xFClt. K\xE9rem ellen\u0151rizze a b\xF6ng\xE9sz\u0151je be\xE1ll\xEDt\xE1sait \xE9s tiltsa le a felugr\xF3 ablakokat blokkol\xF3 alkalmaz\xE1sait erre a honlapra.',
		XmlError		: 'A webszervert\u0151l \xE9rkez\u0151 XML v\xE1lasz nem dolgozhat\xF3 fel megfelel\u0151en.',
		XmlEmpty		: 'A webszervert\u0151l \xE9rkez\u0151 XML v\xE1lasz nem dolgozhat\xF3 fel. A szerver \xFCres v\xE1laszt k\xFCld\xF6tt.',
		XmlRawResponse	: 'A szerver az al\xE1bbi v\xE1laszt adta: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'K\xE9p \xE1tm\xE9retez\xE9se: %s',
		sizeTooBig		: 'Nem adhat\xF3 meg az eredeti f\xE1jln\xE1l nagyobb m\xE9ret (%size).',
		resizeSuccess	: 'A k\xE9p sikeresen \xE1tm\xE9retezve.',
		thumbnailNew	: '\xDAj b\xE9lyegk\xE9p l\xE9trehoz\xE1sa',
		thumbnailSmall	: 'Kicsi (%s)',
		thumbnailMedium	: 'K\xF6zepes (%s)',
		thumbnailLarge	: 'Nagy (%s)',
		newSize			: 'Adja meg az \xFAj m\xE9retet',
		width			: 'Sz\xE9less\xE9g',
		height			: 'Magass\xE1g',
		invalidHeight	: '\xC9rv\xE9nytelen magass\xE1g.',
		invalidWidth	: '\xC9rv\xE9nytelen sz\xE9less\xE9g.',
		invalidName		: '\xC9rv\xE9nytelen f\xE1jln\xE9v.',
		newImage		: 'L\xE9trehoz\xE1s \xFAj fot\xF3k\xE9nt',
		noExtensionChange : 'A f\xE1jl kiterjeszt\xE9se nem v\xE1ltoztathat\xF3.',
		imageSmall		: 'Az eredeti fot\xF3 m\xE9rete t\xFAl kicsi.',
		contextMenuName	: '\xC1tm\xE9retez\xE9s',
		lockRatio		: 'Ar\xE1ny megtart\xE1sa',
		resetSize		: 'Eredeti m\xE9ret'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Ment\xE9s',
		fileOpenError	: 'A f\xE1jl nem nyithat\xF3 meg.',
		fileSaveSuccess	: 'A f\xE1jl sikeresen mentve.',
		contextMenuName	: 'Szerkeszt\xE9s',
		loadingFile		: 'F\xE1jl bet\xF6lt\xE9se, k\xE9rem v\xE1rjon...'
	},

	Maximize :
	{
		maximize : 'Teljes m\xE9ret',
		minimize : 'Kis m\xE9ret'
	},

	Gallery :
	{
		current : 'Fot\xF3: {current} / {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Kicsomagol\xE1s ide',
		extractToLabel		: 'Kicsomagol\xE1s \xFAj mapp\xE1ba...',
		downloadZipLabel	: 'Let\xF6lt\xE9s zip f\xE1jlk\xE9nt',
		compressZipLabel	: 'Becsomagol\xE1s zip f\xE1jlba',
		removeAndExtract	: 'L\xE9tez\u0151 t\xF6rl\xE9se \xE9s kicsomagol\xE1s',
		extractAndOverwrite	: 'L\xE9tez\u0151 fel\xFCl\xEDr\xE1sa \xE9s kicsomagol\xE1s',
		extractSuccess		: 'A f\xE1jl kicsomagol\xE1sa megt\xF6rt\xE9nt.'
	},

	Search :
	{
		searchPlaceholder : 'Keres\xE9s'
	}
};
