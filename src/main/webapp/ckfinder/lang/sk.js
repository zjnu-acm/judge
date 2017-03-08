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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Slovak
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['sk'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedostupn\xE9</span>',
		confirmCancel	: 'Niektor\xE9 mo\u017Enosti boli zmenen\xE9. Naozaj chcete zavrie\u0165 okno?',
		ok				: 'OK',
		cancel			: 'Zru\u0161i\u0165',
		confirmationTitle	: 'Potvrdenie',
		messageTitle	: 'Inform\xE1cia',
		inputTitle		: 'Ot\xE1zka',
		undo			: 'Sp\xE4\u0165',
		redo			: 'Znovu',
		skip			: 'Presko\u010Di\u0165',
		skipAll			: 'Presko\u010Di\u0165 v\u0161etko',
		makeDecision	: 'Ak\xFD \xFAkon sa m\xE1 vykona\u0165?',
		rememberDecision: 'Pam\xE4ta\u0165 si rozhodnutie'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'sk',

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
	DateTime : 'mm/dd/yyyy HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Adres\xE1re',
	FolderLoading	: 'Nahr\xE1vam...',
	FolderNew		: 'Zadajte pros\xEDm meno nov\xE9ho adres\xE1ra: ',
	FolderRename	: 'Zadajte pros\xEDm meno nov\xE9ho adres\xE1ra: ',
	FolderDelete	: 'Skuto\u010Dne zmaza\u0165 adres\xE1r "%1"?',
	FolderRenaming	: ' (Prebieha premenovanie adres\xE1ra...)',
	FolderDeleting	: ' (Prebieha zmazanie adres\xE1ra...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Zadajte pros\xEDm meno nov\xE9ho s\xFAboru: ',
	FileRenameExt	: 'Skuto\u010Dne chcete zmeni\u0165 pr\xEDponu s\xFAboru? Upozornenie: zmenou pr\xEDpony sa s\xFAbor m\xF4\u017Ee sta\u0165 nepou\u017Eite\u013En\xFDm, pokia\u013E pr\xEDpona nie je podporovan\xE1.',
	FileRenaming	: 'Prebieha premenovanie s\xFAboru...',
	FileDelete		: 'Skuto\u010Dne chcete odstr\xE1ni\u0165 s\xFAbor "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Nahr\xE1vam...',
	FilesEmpty		: 'Adres\xE1r je pr\xE1zdny.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Ko\u0161\xEDk',
	BasketClear			: 'Vypr\xE1zdni\u0165 ko\u0161\xEDk',
	BasketRemove		: 'Odstr\xE1ni\u0165 z ko\u0161\xEDka',
	BasketOpenFolder	: 'Otvori\u0165 nadraden\xFD adres\xE1r',
	BasketTruncateConfirm : 'Naozaj chcete odstr\xE1ni\u0165 v\u0161etky s\xFAbory z ko\u0161\xEDka?',
	BasketRemoveConfirm	: 'Naozaj chcete odstr\xE1ni\u0165 s\xFAbor "%1" z ko\u0161\xEDka?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'V ko\u0161\xEDku nie s\xFA \u017Eiadne s\xFAbory, potiahnite a vlo\u017Ete nejak\xFD.',
	BasketCopyFilesHere	: 'Prekop\xEDrova\u0165 s\xFAbory z ko\u0161\xEDka',
	BasketMoveFilesHere	: 'Presun\xFA\u0165 s\xFAbory z ko\u0161\xEDka',

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
	Upload		: 'Prekop\xEDrova\u0165 na server (Upload)',
	UploadTip	: 'Prekop\xEDrova\u0165 nov\xFD s\xFAbor',
	Refresh		: 'Znovuna\u010D\xEDta\u0165 (Refresh)',
	Settings	: 'Nastavenia',
	Help		: 'Pomoc',
	HelpTip		: 'Pomoc',

	// Context Menus
	Select			: 'Vybra\u0165',
	SelectThumbnail : 'Zvo\u013Ete miniat\xFAru',
	View			: 'N\xE1h\u013Ead',
	Download		: 'Stiahnu\u0165',

	NewSubFolder	: 'Nov\xFD podadres\xE1r',
	Rename			: 'Premenova\u0165',
	Delete			: 'Zmaza\u0165',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Prekop\xEDrova\u0165 sem',
	MoveDragDrop	: 'Presun\xFA\u0165 sem',

	// Dialogs
	RenameDlgTitle		: 'Premenova\u0165',
	NewNameDlgTitle		: 'Nov\xE9 meno',
	FileExistsDlgTitle	: 'S\xFAbor u\u017E existuje',
	SysErrorDlgTitle : 'Syst\xE9mov\xE1 chyba',

	FileOverwrite	: 'Prep\xEDsa\u0165',
	FileAutorename	: 'Auto-premenovanie',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Zru\u0161i\u0165',
	CloseBtn	: 'Zatvori\u0165',

	// Upload Panel
	UploadTitle			: 'Nahra\u0165 nov\xFD s\xFAbor',
	UploadSelectLbl		: 'Vyberte s\xFAbor, ktor\xFD chcete prekop\xEDrova\u0165 na server',
	UploadProgressLbl	: '(Prebieha kop\xEDrovanie, \u010Dakajte pros\xEDm...)',
	UploadBtn			: 'Prekop\xEDrova\u0165 vybrat\xFD s\xFAbor',
	UploadBtnCancel		: 'Zru\u0161i\u0165',

	UploadNoFileMsg		: 'Vyberte pros\xEDm s\xFAbor na Va\u0161om po\u010D\xEDta\u010Di!',
	UploadNoFolder		: 'Pred n\xE1hr\xE1van\xEDm zvo\u013Ete adres\xE1r, pros\xEDm',
	UploadNoPerms		: 'Nahratie s\xFAboru nie je povolen\xE9.',
	UploadUnknError		: 'V priebehu posielania s\xFAboru sa vyskytla chyba.',
	UploadExtIncorrect	: 'V tomto adres\xE1ri nie je povolen\xFD tento form\xE1t s\xFAboru.',

	// Flash Uploads
	UploadLabel			: 'S\xFAbory k nahratiu',
	UploadTotalFiles	: 'V\u0161etky s\xFAbory:',
	UploadTotalSize		: 'Celkov\xE1 ve\u013Ekos\u0165:',
	UploadSend			: 'Prekop\xEDrova\u0165 na server',
	UploadAddFiles		: 'Prida\u0165 s\xFAbory',
	UploadClearFiles	: 'Vy\u010Disti\u0165 s\xFAbory',
	UploadCancel		: 'Zru\u0161i\u0165 nahratie',
	UploadRemove		: 'Odstr\xE1ni\u0165',
	UploadRemoveTip		: 'Odstr\xE1ni\u0165 !f',
	UploadUploaded		: 'Nahrat\xE9 !n%',
	UploadProcessing	: 'Spracov\xE1va sa ...',

	// Settings Panel
	SetTitle		: 'Nastavenia',
	SetView			: 'N\xE1h\u013Ead:',
	SetViewThumb	: 'Miniobr\xE1zky',
	SetViewList		: 'Zoznam',
	SetDisplay		: 'Zobrazi\u0165:',
	SetDisplayName	: 'N\xE1zov s\xFAboru',
	SetDisplayDate	: 'D\xE1tum',
	SetDisplaySize	: 'Ve\u013Ekos\u0165 s\xFAboru',
	SetSort			: 'Zoradenie:',
	SetSortName		: 'pod\u013Ea n\xE1zvu s\xFAboru',
	SetSortDate		: 'pod\u013Ea d\xE1tumu',
	SetSortSize		: 'pod\u013Ea ve\u013Ekosti',
	SetSortExtension		: 'pod\u013Ea form\xE1tu',

	// Status Bar
	FilesCountEmpty : '<Pr\xE1zdny adres\xE1r>',
	FilesCountOne	: '1 s\xFAbor',
	FilesCountMany	: '%1 s\xFAborov',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Server nemohol dokon\u010Di\u0165 spracovanie po\u017Eiadavky. (Chyba %1)',
	Errors :
	{
	 10 : 'Neplatn\xFD pr\xEDkaz.',
	 11 : 'V po\u017Eiadavke nebol \u0161pecifikovan\xFD typ s\xFAboru.',
	 12 : 'Nepodporovan\xFD typ s\xFAboru.',
	102 : 'Neplatn\xFD n\xE1zov s\xFAboru alebo adres\xE1ra.',
	103 : 'Nebolo mo\u017En\xE9 dokon\u010Di\u0165 spracovanie po\u017Eiadavky kv\xF4li neposta\u010Duj\xFAcej \xFArovni opr\xE1vnen\xED.',
	104 : 'Nebolo mo\u017En\xE9 dokon\u010Di\u0165 spracovanie po\u017Eiadavky kv\xF4li obmedzeniam v pr\xEDstupov\xFDch pr\xE1vach k s\xFAborom.',
	105 : 'Neplatn\xE1 pr\xEDpona s\xFAboru.',
	109 : 'Neplatn\xE1 po\u017Eiadavka.',
	110 : 'Neidentifikovan\xE1 chyba.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Zadan\xFD s\xFAbor alebo adres\xE1r u\u017E existuje.',
	116 : 'Adres\xE1r nebol n\xE1jden\xFD. Aktualizujte obsah adres\xE1ra (Znovuna\u010D\xEDta\u0165) a sk\xFAste znovu.',
	117 : 'S\xFAbor nebol n\xE1jden\xFD. Aktualizujte obsah adres\xE1ra (Znovuna\u010D\xEDta\u0165) a sk\xFAste znovu.',
	118 : 'Zdrojov\xE9 a cie\u013Eov\xE9 cesty s\xFA rovnak\xE9.',
	201 : 'S\xFAbor so zadan\xFDm n\xE1zvom u\u017E existuje. Prekop\xEDrovan\xFD s\xFAbor bol premenovan\xFD na "%1".',
	202 : 'Neplatn\xFD s\xFAbor.',
	203 : 'Neplatn\xFD s\xFAbor - s\xFAbor presahuje maxim\xE1lnu povolen\xFA ve\u013Ekos\u0165.',
	204 : 'Kop\xEDrovan\xFD s\xFAbor je po\u0161koden\xFD.',
	205 : 'Server nem\xE1 \u0161pecifikovan\xFD do\u010Dasn\xFD adres\xE1r pre kop\xEDrovan\xE9 s\xFAbory.',
	206 : 'Kop\xEDrovanie preru\u0161en\xE9 kv\xF4li nedostato\u010Dn\xE9mu zabezpe\u010Deniu. S\xFAbor obsahuje HTML data.',
	207 : 'Prekop\xEDrovan\xFD s\xFAbor bol premenovan\xFD na "%1".',
	300 : 'Presunutie s\xFAborov zlyhalo.',
	301 : 'Kop\xEDrovanie s\xFAborov zlyhalo.',
	500 : 'Prehliadanie s\xFAborov je zak\xE1zan\xE9 kv\xF4li bezpe\u010Dnosti. Kontaktujte pros\xEDm administr\xE1tora a overte nastavenia v konfigura\u010Dnom s\xFAbore pre CKFinder.',
	501 : 'Moment\xE1lne nie je zapnut\xE1 podpora pre gener\xE1ciu miniobr\xE1zkov.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'N\xE1zov s\xFAboru nesmie by\u0165 pr\xE1zdne.',
		FileExists		: 'S\xFAbor %s u\u017E existuje.',
		FolderEmpty		: 'N\xE1zov adres\xE1ra nesmie by\u0165 pr\xE1zdny.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'S\xFAbor nesmie obsahova\u0165 \u017Eiadny z nasleduj\xFAcich znakov: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Adres\xE1r nesmie obsahova\u0165 \u017Eiadny z nasleduj\xFAcich znakov: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nebolo mo\u017En\xE9 otvori\u0165 s\xFAbor v novom okne. Overte nastavenia V\xE1\u0161ho prehliada\u010Da a zak\xE1\u017Ete v\u0161etky blokova\u010De popup okien pre t\xFAto webstr\xE1nku.',
		XmlError		: 'Nebolo mo\u017En\xE9 korektne na\u010D\xEDta\u0165 XML odozvu z web serveu.',
		XmlEmpty		: 'Nebolo mo\u017En\xE9 korektne na\u010D\xEDta\u0165 XML odozvu z web serveu. Server vr\xE1til pr\xE1zdnu odpove\u010F (odozvu).',
		XmlRawResponse	: 'Neupraven\xE1 odpove\u010F zo servera: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Zmeni\u0165 ve\u013Ekos\u0165 %s',
		sizeTooBig		: 'Nie je mo\u017En\xE9 nastavi\u0165 v\xFD\u0161ku alebo \u0161\xEDrku obr\xE1zku na v\xE4\u010D\u0161ie hodnoty ako origin\xE1lnu ve\u013Ekos\u0165 (%size).',
		resizeSuccess	: 'Zmena v\u013Ekosti obr\xE1zku bola \xFAspe\u0161ne vykonan\xE1.',
		thumbnailNew	: 'Vytvori\u0165 nov\xFA miniat\xFAru obr\xE1zku',
		thumbnailSmall	: 'Mal\xFD (%s)',
		thumbnailMedium	: 'Stredn\xFD (%s)',
		thumbnailLarge	: 'Ve\u013Ek\xFD (%s)',
		newSize			: 'Nastavi\u0165 nov\xFA ve\u013Ekos\u0165',
		width			: '\u0160\xEDrka',
		height			: 'V\xFD\u0161ka',
		invalidHeight	: 'Neplatn\xE1 v\xFD\u0161ka.',
		invalidWidth	: 'Neplatn\xE1 \u0161\xEDrka.',
		invalidName		: 'Neplatn\xFD n\xE1zov s\xFAboru.',
		newImage		: 'Vytvori\u0165 nov\xFD obr\xE1zok',
		noExtensionChange : 'Nie je mo\u017En\xE9 zmeni\u0165 form\xE1t s\xFAboru.',
		imageSmall		: 'Zdrojov\xFD obr\xE1zok je ve\u013Emi mal\xFD.',
		contextMenuName	: 'Zmeni\u0165 ve\u013Ekos\u0165',
		lockRatio		: 'Z\xE1mok',
		resetSize		: 'P\xF4vodn\xE1 ve\u013Ekos\u0165'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Ulo\u017Ei\u0165',
		fileOpenError	: 'Nie je mo\u017En\xE9 otvori\u0165 s\xFAbor.',
		fileSaveSuccess	: 'S\xFAbor bol \xFAspe\u0161ne ulo\u017Een\xFD.',
		contextMenuName	: 'Upravi\u0165',
		loadingFile		: 'S\xFAbor sa nahr\xE1va, pros\xEDm \u010Daka\u0165...'
	},

	Maximize :
	{
		maximize : 'Maximalizova\u0165',
		minimize : 'Minimalizova\u0165'
	},

	Gallery :
	{
		current : 'Obr\xE1zok {current} z {total}'
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
		searchPlaceholder : 'H\u013Eada\u0165'
	}
};
