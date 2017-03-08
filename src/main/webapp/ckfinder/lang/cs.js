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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Czech
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['cs'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedostupn\xE9</span>',
		confirmCancel	: 'N\u011Bkter\xE1 z nastaven\xED byla zm\u011Bn\u011Bna. Skute\u010Dn\u011B chcete dialogov\xE9 okno zav\u0159\xEDt?',
		ok				: 'OK',
		cancel			: 'Zru\u0161it',
		confirmationTitle	: 'Potvrzen\xED',
		messageTitle	: 'Informace',
		inputTitle		: 'Ot\xE1zka',
		undo			: 'Zp\u011Bt',
		redo			: 'Znovu',
		skip			: 'P\u0159esko\u010Dit',
		skipAll			: 'P\u0159esko\u010Dit v\u0161e',
		makeDecision	: 'Co by se m\u011Blo prov\xE9st?',
		rememberDecision: 'Zapamatovat si m\xE9 rozhodnut\xED'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'cs',
	LangCode : 'cs',

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
	DateTime : 'd/m/yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Slo\u017Eky',
	FolderLoading	: 'Na\u010D\xEDt\xE1n\xED...',
	FolderNew		: 'Zadejte n\xE1zev nov\xE9 slo\u017Eky: ',
	FolderRename	: 'Zadejte nov\xFD n\xE1zev slo\u017Eky: ',
	FolderDelete	: 'Opravdu chcete slo\u017Eku "%1" smazat?',
	FolderRenaming	: ' (P\u0159ejmenov\xE1v\xE1n\xED...)',
	FolderDeleting	: ' (Maz\xE1n\xED...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Zadejte nov\xFD n\xE1zev souboru: ',
	FileRenameExt	: 'Opravdu chcete zm\u011Bnit p\u0159\xEDponu souboru? Soubor se m\u016F\u017Ee st\xE1t nepou\u017Eiteln\xFDm.',
	FileRenaming	: 'P\u0159ejmenov\xE1v\xE1n\xED...',
	FileDelete		: 'Opravdu chcete smazat soubor "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Na\u010D\xEDt\xE1n\xED...',
	FilesEmpty		: 'Pr\xE1zdn\xE1 slo\u017Eka.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Ko\u0161\xEDk',
	BasketClear			: 'Vy\u010Distit Ko\u0161\xEDk',
	BasketRemove		: 'Odstranit z Ko\u0161\xEDku',
	BasketOpenFolder	: 'Otev\u0159\xEDt nad\u0159azenou slo\u017Eku',
	BasketTruncateConfirm : 'Opravdu chcete z Ko\u0161\xEDku odstranit v\u0161echny soubory?',
	BasketRemoveConfirm	: 'Opravdu chcete odstranit soubor "%1" z Ko\u0161\xEDku?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'V Ko\u0161\xEDku nejsou \u017E\xE1dn\xE9 soubory, tak sem n\u011Bkter\xE9 p\u0159et\xE1hn\u011Bte.',
	BasketCopyFilesHere	: 'Kop\xEDrovat soubory z Ko\u0161\xEDku',
	BasketMoveFilesHere	: 'P\u0159esunout soubory z Ko\u0161\xEDku',

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
	Upload		: 'Nahr\xE1t',
	UploadTip	: 'Nahr\xE1t nov\xFD soubor',
	Refresh		: 'Znovu na\u010D\xEDst',
	Settings	: 'Nastaven\xED',
	Help		: 'N\xE1pov\u011Bda',
	HelpTip		: 'N\xE1pov\u011Bda',

	// Context Menus
	Select			: 'Vybrat',
	SelectThumbnail : 'Vybrat n\xE1hled',
	View			: 'Zobrazit',
	Download		: 'Ulo\u017Eit jako',

	NewSubFolder	: 'Nov\xE1 podslo\u017Eka',
	Rename			: 'P\u0159ejmenovat',
	Delete			: 'Smazat',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Zkop\xEDrovat sem',
	MoveDragDrop	: 'P\u0159esunout sem',

	// Dialogs
	RenameDlgTitle		: 'P\u0159ejmenovat',
	NewNameDlgTitle		: 'Nov\xFD n\xE1zev',
	FileExistsDlgTitle	: 'Soubor ji\u017E existuje',
	SysErrorDlgTitle : 'Chyba syst\xE9mu',

	FileOverwrite	: 'P\u0159epsat',
	FileAutorename	: 'Automaticky p\u0159ejmenovat',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Zru\u0161it',
	CloseBtn	: 'Zav\u0159\xEDt',

	// Upload Panel
	UploadTitle			: 'Nahr\xE1t nov\xFD soubor',
	UploadSelectLbl		: 'Zvolit soubor k nahr\xE1n\xED',
	UploadProgressLbl	: '(Prob\xEDh\xE1 nahr\xE1v\xE1n\xED, \u010Dekejte...)',
	UploadBtn			: 'Nahr\xE1t zvolen\xFD soubor',
	UploadBtnCancel		: 'Zru\u0161it',

	UploadNoFileMsg		: 'Vyberte pros\xEDm soubor z Va\u0161eho po\u010D\xEDta\u010De.',
	UploadNoFolder		: 'P\u0159ed nahr\xE1v\xE1n\xEDm vyberte slo\u017Eku pros\xEDm.',
	UploadNoPerms		: 'Nahr\xE1v\xE1n\xED soubor\u016F nen\xED povoleno.',
	UploadUnknError		: 'Chyba p\u0159i pos\xEDl\xE1n\xED souboru.',
	UploadExtIncorrect	: 'P\u0159\xEDpona souboru nen\xED v t\xE9to slo\u017Ece povolena.',

	// Flash Uploads
	UploadLabel			: 'Soubory k nahr\xE1n\xED',
	UploadTotalFiles	: 'Celkem soubor\u016F:',
	UploadTotalSize		: 'Celkov\xE1 velikost:',
	UploadSend			: 'Nahr\xE1t',
	UploadAddFiles		: 'P\u0159idat soubory',
	UploadClearFiles	: 'Vy\u010Distit soubory',
	UploadCancel		: 'Zru\u0161it nahr\xE1v\xE1n\xED',
	UploadRemove		: 'Odstranit',
	UploadRemoveTip		: 'Odstranit !f',
	UploadUploaded		: 'Nahr\xE1no !n%',
	UploadProcessing	: 'Zpracov\xE1v\xE1n\xED...',

	// Settings Panel
	SetTitle		: 'Nastaven\xED',
	SetView			: 'Zobrazen\xED:',
	SetViewThumb	: 'N\xE1hled',
	SetViewList		: 'Seznam',
	SetDisplay		: 'Zobrazit:',
	SetDisplayName	: 'N\xE1zev',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Velikost',
	SetSort			: 'Se\u0159azen\xED:',
	SetSortName		: 'Podle n\xE1zvu',
	SetSortDate		: 'Podle data',
	SetSortSize		: 'Podle velikosti',
	SetSortExtension		: 'Podle p\u0159\xEDpony',

	// Status Bar
	FilesCountEmpty : '<Pr\xE1zdn\xE1 slo\u017Eka>',
	FilesCountOne	: '1 soubor',
	FilesCountMany	: '%1 soubor\u016F',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'P\u0159\xEDkaz nebylo mo\u017En\xE9 dokon\u010Dit. (Chyba %1)',
	Errors :
	{
	 10 : 'Neplatn\xFD p\u0159\xEDkaz.',
	 11 : 'Typ zdroje nebyl v po\u017Eadavku ur\u010Den.',
	 12 : 'Po\u017Eadovan\xFD typ zdroje nen\xED platn\xFD.',
	102 : '\u0160patn\xE9 n\xE1zev souboru, nebo slo\u017Eky.',
	103 : 'Nebylo mo\u017En\xE9 p\u0159\xEDkaz dokon\u010Dit kv\u016Fli omezen\xED opr\xE1vn\u011Bn\xED.',
	104 : 'Nebylo mo\u017En\xE9 p\u0159\xEDkaz dokon\u010Dit kv\u016Fli omezen\xED opr\xE1vn\u011Bn\xED souborov\xE9ho syst\xE9mu.',
	105 : 'Neplatn\xE1 p\u0159\xEDpona souboru.',
	109 : 'Neplatn\xFD po\u017Eadavek.',
	110 : 'Nezn\xE1m\xE1 chyba.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Soubor nebo slo\u017Eka se stejn\xFDm n\xE1zvem ji\u017E existuje.',
	116 : 'Slo\u017Eka nenalezena, pros\xEDm obnovte a zkuste znovu.',
	117 : 'Soubor nenalezen, pros\xEDm obnovte seznam soubor\u016F a zkuste znovu.',
	118 : 'Cesty zdroje a c\xEDle jsou stejn\xE9.',
	201 : 'Soubor se stejn\xFDm n\xE1zvem je ji\u017E dostupn\xFD, nahran\xFD soubor byl p\u0159ejmenov\xE1n na "%1".',
	202 : 'Neplatn\xFD soubor.',
	203 : 'Neplatn\xFD soubor. Velikost souboru je p\u0159\xEDli\u0161 velk\xE1.',
	204 : 'Nahran\xFD soubor je po\u0161kozen.',
	205 : 'Na serveru nen\xED dostupn\xE1 do\u010Dasn\xE1 slo\u017Eka pro nahr\xE1v\xE1n\xED.',
	206 : 'Nahr\xE1v\xE1n\xED zru\u0161eno z bezpe\u010Dnostn\xEDch d\u016Fvod\u016F. Soubor obsahuje data podobn\xE1 HTML.',
	207 : 'Nahran\xFD soubor byl p\u0159ejmenov\xE1n na "%1".',
	300 : 'P\u0159esunov\xE1n\xED souboru(\u016F) selhalo.',
	301 : 'Kop\xEDrov\xE1n\xED souboru(\u016F) selhalo.',
	500 : 'Pr\u016Fzkumn\xEDk soubor\u016F je z bezpe\u010Dnostn\xEDch d\u016Fvod\u016F zak\xE1z\xE1n. Zd\u011Blte to pros\xEDm spr\xE1vci syst\xE9mu a zkontrolujte soubor nastaven\xED CKFinder.',
	501 : 'Podpora n\xE1hled\u016F je zak\xE1z\xE1na.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'N\xE1zev souboru nem\u016F\u017Ee b\xFDt pr\xE1zdn\xFD.',
		FileExists		: 'Soubor %s ji\u017E existuje.',
		FolderEmpty		: 'N\xE1zev slo\u017Eky nem\u016F\u017Ee b\xFDt pr\xE1zdn\xFD.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'N\xE1zev souboru nesm\xED obsahovat n\xE1sleduj\xEDc\xED znaky: \n\\ / : * ? " < > |',
		FolderInvChar	: 'N\xE1zev slo\u017Eky nesm\xED obsahovat n\xE1sleduj\xEDc\xED znaky: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Soubor nebylo mo\u017En\xE9 otev\u0159\xEDt do nov\xE9ho okna. Pros\xEDm nastavte si V\xE1\u0161 prohl\xED\u017Ee\u010D a zaka\u017Ete ve\u0161ker\xE9 blokov\xE1n\xED vyskakovac\xEDch oken.',
		XmlError		: 'Nebylo mo\u017En\xE9 spr\xE1vn\u011B na\u010D\xEDst XML odpov\u011B\u010F z internetov\xE9ho serveru.',
		XmlEmpty		: 'Nebylo mo\u017En\xE9 na\u010D\xEDst XML odpov\u011B\u010F z internetov\xE9ho serveru. Server vr\xE1til pr\xE1zdnou odpov\u011B\u010F.',
		XmlRawResponse	: '\u010Cist\xE1 odpov\u011B\u010F od serveru: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Zm\u011Bnit velikost %s',
		sizeTooBig		: 'Nelze nastavit \u0161\xED\u0159ku \u010Di v\xFD\u0161ku obr\xE1zku na hodnotu vy\u0161\u0161\xED ne\u017E p\u016Fvodn\xED velikost (%size).',
		resizeSuccess	: '\xDAsp\u011B\u0161n\u011B zm\u011Bn\u011Bna velikost obr\xE1zku.',
		thumbnailNew	: 'Vytvo\u0159it nov\xFD n\xE1hled',
		thumbnailSmall	: 'Mal\xFD (%s)',
		thumbnailMedium	: 'St\u0159edn\xED (%s)',
		thumbnailLarge	: 'Velk\xFD (%s)',
		newSize			: 'Nastavit novou velikost',
		width			: '\u0160\xED\u0159ka',
		height			: 'V\xFD\u0161ka',
		invalidHeight	: 'Neplatn\xE1 v\xFD\u0161ka.',
		invalidWidth	: 'Neplatn\xE1 \u0161\xED\u0159ka.',
		invalidName		: 'Neplatn\xFD n\xE1zev souboru.',
		newImage		: 'Vytvo\u0159it nov\xFD obr\xE1zek',
		noExtensionChange : 'P\u0159\xEDponu souboru nelze zm\u011Bnit.',
		imageSmall		: 'Zdrojov\xFD obr\xE1zek je p\u0159\xEDli\u0161 mal\xFD.',
		contextMenuName	: 'Zm\u011Bnit velikost',
		lockRatio		: 'Uzamknout pom\u011Br',
		resetSize		: 'P\u016Fvodn\xED velikost'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Ulo\u017Eit',
		fileOpenError	: 'Soubor nelze otev\u0159\xEDt.',
		fileSaveSuccess	: 'Soubor \xFAsp\u011B\u0161n\u011B ulo\u017Een.',
		contextMenuName	: 'Upravit',
		loadingFile		: 'Na\u010D\xEDt\xE1n\xED souboru, \u010Dekejte pros\xEDm...'
	},

	Maximize :
	{
		maximize : 'Maximalizovat',
		minimize : 'Minimalizovat'
	},

	Gallery :
	{
		current : 'Obr\xE1zek {current} z {total}'
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
		searchPlaceholder : 'Hledat'
	}
};
