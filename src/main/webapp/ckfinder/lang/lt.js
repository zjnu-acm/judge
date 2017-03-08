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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Lithuanian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['lt'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, n\u0117ra</span>',
		confirmCancel	: 'Kai kurie nustatymai buvo pakeisti. Ar tikrai norite u\u017Edaryti \u0161\u012F lang\u0105?',
		ok				: 'Gerai',
		cancel			: 'At\u0161aukti',
		confirmationTitle	: 'Patvirtinimas',
		messageTitle	: 'Informacija',
		inputTitle		: 'Klausimas',
		undo			: 'Veiksmas atgal',
		redo			: 'Veiksmas pirmyn',
		skip			: 'Praleisti',
		skipAll			: 'Praleisti visk\u0105',
		makeDecision	: 'K\u0105 pasirinksite?',
		rememberDecision: 'Atsiminti mano pasirinkim\u0105'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'lt',
	LangCode : 'lt',

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
	DateTime : 'yyyy.mm.dd H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Segtuvai',
	FolderLoading	: 'Pra\u0161au palaukite...',
	FolderNew		: 'Pra\u0161au \u012Fra\u0161ykite naujo segtuvo pavadinim\u0105: ',
	FolderRename	: 'Pra\u0161au \u012Fra\u0161ykite naujo segtuvo pavadinim\u0105: ',
	FolderDelete	: 'Ar tikrai norite i\u0161trinti "%1" segtuv\u0105?',
	FolderRenaming	: ' (Pervadinama...)',
	FolderDeleting	: ' (Trinama...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Pra\u0161au \u012Fra\u0161ykite naujo failo pavadinim\u0105: ',
	FileRenameExt	: 'Ar tikrai norite pakeisti \u0161io failo pl\u0117tin\u012F? Failas gali b\u016Bti nebepanaudojamas',
	FileRenaming	: 'Pervadinama...',
	FileDelete		: 'Ar tikrai norite i\u0161trinti fail\u0105 "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Pra\u0161au palaukite...',
	FilesEmpty		: 'Tu\u0161\u010Dias segtuvas',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Krep\u0161elis',
	BasketClear			: 'I\u0161tu\u0161tinti krep\u0161el\u012F',
	BasketRemove		: 'I\u0161trinti krep\u0161el\u012F',
	BasketOpenFolder	: 'Atidaryti failo segtuv\u0105',
	BasketTruncateConfirm : 'Ar tikrai norite i\u0161trinti visus failus i\u0161 krep\u0161elio?',
	BasketRemoveConfirm	: 'Ar tikrai norite i\u0161trinti fail\u0105 "%1" i\u0161 krep\u0161elio?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Krep\u0161elyje fail\u0173 n\u0117ra, nuvilkite ir \u012Fmeskite juos \u012F krep\u0161el\u012F.',
	BasketCopyFilesHere	: 'Kopijuoti failus i\u0161 krep\u0161elio',
	BasketMoveFilesHere	: 'Perkelti failus i\u0161 krep\u0161elio',

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
	Upload		: '\u012Ekelti',
	UploadTip	: '\u012Ekelti nauj\u0105 fail\u0105',
	Refresh		: 'Atnaujinti',
	Settings	: 'Nustatymai',
	Help		: 'Pagalba',
	HelpTip		: 'Patarimai',

	// Context Menus
	Select			: 'Pasirinkti',
	SelectThumbnail : 'Pasirinkti miniati\u016Br\u0105',
	View			: 'Per\u017Ei\u016Br\u0117ti',
	Download		: 'Atsisi\u0173sti',

	NewSubFolder	: 'Naujas segtuvas',
	Rename			: 'Pervadinti',
	Delete			: 'I\u0161trinti',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Nukopijuoti \u010Dia',
	MoveDragDrop	: 'Perkelti \u010Dia',

	// Dialogs
	RenameDlgTitle		: 'Pervadinti',
	NewNameDlgTitle		: 'Naujas pavadinimas',
	FileExistsDlgTitle	: 'Toks failas jau egzistuoja',
	SysErrorDlgTitle : 'Sistemos klaida',

	FileOverwrite	: 'U\u017Era\u0161yti ant vir\u0161aus',
	FileAutorename	: 'Automati\u0161kai pervadinti',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Gerai',
	CancelBtn	: 'At\u0161aukti',
	CloseBtn	: 'U\u017Edaryti',

	// Upload Panel
	UploadTitle			: '\u012Ekelti nauj\u0105 fail\u0105',
	UploadSelectLbl		: 'Pasirinkite fail\u0105 \u012Fk\u0117limui',
	UploadProgressLbl	: '(Vykdomas \u012Fk\u0117limas, pra\u0161au palaukite...)',
	UploadBtn			: '\u012Ekelti pasirinkt\u0105 fail\u0105',
	UploadBtnCancel		: 'At\u0161aukti',

	UploadNoFileMsg		: 'Pasirinkite fail\u0105 i\u0161 savo kompiuterio',
	UploadNoFolder		: 'Pasirinkite segtuv\u0105 prie\u0161 \u012Fkeliant.',
	UploadNoPerms		: 'Fail\u0173 \u012Fk\u0117limas u\u017Edraustas.',
	UploadUnknError		: '\u012Evyko klaida siun\u010Diant fail\u0105.',
	UploadExtIncorrect	: '\u0160iame segtuve toks fail\u0173 pl\u0117tinys yra u\u017Edraustas.',

	// Flash Uploads
	UploadLabel			: '\u012Ekeliami failai',
	UploadTotalFiles	: 'I\u0161 viso fail\u0173:',
	UploadTotalSize		: 'Visa apimtis:',
	UploadSend			: '\u012Ekelti',
	UploadAddFiles		: 'Prid\u0117ti failus',
	UploadClearFiles	: 'I\u0161valyti failus',
	UploadCancel		: 'At\u0161aukti nusiuntim\u0105',
	UploadRemove		: 'Pa\u0161alinti',
	UploadRemoveTip		: 'Pa\u0161alinti !f',
	UploadUploaded		: '\u012Ekeltas !n%',
	UploadProcessing	: 'Apdorojama...',

	// Settings Panel
	SetTitle		: 'Nustatymai',
	SetView			: 'Per\u017Ei\u016Br\u0117ti:',
	SetViewThumb	: 'Miniati\u016Bros',
	SetViewList		: 'S\u0105ra\u0161as',
	SetDisplay		: 'Rodymas:',
	SetDisplayName	: 'Failo pavadinimas',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Failo dydis',
	SetSort			: 'R\u016B\u0161iavimas:',
	SetSortName		: 'pagal failo pavadinim\u0105',
	SetSortDate		: 'pagal dat\u0105',
	SetSortSize		: 'pagal apimt\u012F',
	SetSortExtension		: 'pagal pl\u0117tin\u012F',

	// Status Bar
	FilesCountEmpty : '<Tu\u0161\u010Dias segtuvas>',
	FilesCountOne	: '1 failas',
	FilesCountMany	: '%1 failai',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'U\u017Eklausos \u012Fvykdyti nepavyko. (Klaida %1)',
	Errors :
	{
	 10 : 'Neteisinga komanda.',
	 11 : 'Resurso r\u016B\u0161is nenurodyta u\u017Eklausoje.',
	 12 : 'Neteisinga resurso r\u016B\u0161is.',
	102 : 'Netinkamas failas arba segtuvo pavadinimas.',
	103 : 'Nepavyko \u012Fvykdyti u\u017Eklausos d\u0117l autorizavimo apribojim\u0173.',
	104 : 'Nepavyko \u012Fvykdyti u\u017Eklausos d\u0117l fail\u0173 sistemos leidim\u0173 apribojim\u0173.',
	105 : 'Netinkamas failo pl\u0117tinys.',
	109 : 'Netinkama u\u017Eklausa.',
	110 : 'Ne\u017Einoma klaida.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Failas arba segtuvas su tuo pa\u010Diu pavadinimu jau yra.',
	116 : 'Segtuvas nerastas. Pabandykite atnaujinti.',
	117 : 'Failas nerastas. Pabandykite atnaujinti fail\u0173 s\u0105ra\u0161\u0105.',
	118 : '\u0160altinio ir nurodomos vietos nuorodos yra vienodos.',
	201 : 'Failas su tuo pa\u010Diu pavadinimu jau tra. \u012Ekeltas failas buvo pervadintas \u012F "%1"',
	202 : 'Netinkamas failas',
	203 : 'Netinkamas failas. Failo apimtis yra per didel\u0117.',
	204 : '\u012Ekeltas failas yra pa\u017Eeistas.',
	205 : 'N\u0117ra laikinojo segtuvo skirto failams \u012Fkelti.',
	206 : '\u012Ek\u0117limas bus nutrauktas d\u0117l saugumo sumetim\u0173. \u0160iame faile yra HTML duomenys.',
	207 : '\u012Ekeltas failas buvo pervadintas \u012F "%1"',
	300 : 'Fail\u0173 perk\u0117limas nepavyko.',
	301 : 'Fail\u0173 kopijavimas nepavyko.',
	500 : 'Fail\u0173 nar\u0161ykl\u0117 yra i\u0161jungta d\u0117l saugumo nustaym\u0173. Pra\u0161au susisiekti su sistem\u0173 administratoriumi ir patikrinkite CKFinder konfig\u016Bracin\u012F fail\u0105.',
	501 : 'Miniati\u016Br\u0173 palaikymas i\u0161jungtas.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Failo pavadinimas negali b\u016Bti tu\u0161\u010Dias',
		FileExists		: 'Failas %s jau egzistuoja',
		FolderEmpty		: 'Segtuvo pavadinimas negali b\u016Bti tu\u0161\u010Dias',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Failo pavadinimas negali tur\u0117ti bent vieno i\u0161 \u0161i\u0173 simboli\u0173: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Segtuvo pavadinimas negali tur\u0117ti bent vieno i\u0161 \u0161i\u0173 simboli\u0173: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nepavyko atidaryti failo naujame lange. Pra\u0161au pakeiskite savo nar\u0161ykl\u0117s nustatymus, kad b\u016Bt\u0173 leid\u017Eiami i\u0161kylantys langai \u0161iame tinklapyje.',
		XmlError		: 'Nepavyko \u012Fkrauti XML atsako i\u0161 web serverio.',
		XmlEmpty		: 'Nepavyko \u012Fkrauti XML atsako i\u0161 web serverio. Serveris gra\u017Eino tu\u0161\u010Di\u0105 u\u017Eklaus\u0105.',
		XmlRawResponse	: 'Vientisas atsakas i\u0161 serverio: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Keisti matmenis %s',
		sizeTooBig		: 'Negalima nustatyti auk\u0161\u010Dio ir plo\u010Dio \u012F didesnius nei originalaus paveiksliuko (%size).',
		resizeSuccess	: 'Paveiksliuko matmenys pakeisti.',
		thumbnailNew	: 'Sukurti nauj\u0105 miniati\u016Br\u0105',
		thumbnailSmall	: 'Ma\u017Eas (%s)',
		thumbnailMedium	: 'Vidutinis (%s)',
		thumbnailLarge	: 'Didelis (%s)',
		newSize			: 'Nustatyti naujus matmenis',
		width			: 'Plotis',
		height			: 'Auk\u0161tis',
		invalidHeight	: 'Neteisingas auk\u0161tis.',
		invalidWidth	: 'Neteisingas plotis.',
		invalidName		: 'Neteisingas pavadinimas.',
		newImage		: 'Sukurti nauj\u0105 paveiksliuk\u0105',
		noExtensionChange : 'Failo pl\u0117tinys negali b\u016Bti pakeistas.',
		imageSmall		: '\u0160altinio paveiksliukas yra per ma\u017Eas',
		contextMenuName	: 'Pakeisti matmenis',
		lockRatio		: 'I\u0161laikyti matmen\u0173 santyk\u012F',
		resetSize		: 'Nustatyti dyd\u012F i\u0161 naujo'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'I\u0161saugoti',
		fileOpenError	: 'Nepavyko atidaryti failo.',
		fileSaveSuccess	: 'Failas s\u0117kmingai i\u0161saugotas.',
		contextMenuName	: 'Redaguoti',
		loadingFile		: '\u012Ekraunamas failas, pra\u0161au palaukite...'
	},

	Maximize :
	{
		maximize : 'Padidinti',
		minimize : 'Suma\u017Einti'
	},

	Gallery :
	{
		current : 'Nuotrauka {current} i\u0161 {total}'
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
		searchPlaceholder : 'Paie\u0161ka'
	}
};
