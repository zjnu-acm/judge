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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Romanian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['ro'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, indisponibil</span>',
		confirmCancel	: 'Unele op\u021Biuni au fost schimbate. E\u0219ti sigur c\u0103 vrei s\u0103 \xEEnchizi fereastra de dialog?',
		ok				: 'OK',
		cancel			: 'Anuleaz\u0103',
		confirmationTitle	: 'Confirm\u0103',
		messageTitle	: 'Informa\u021Bii',
		inputTitle		: '\xCEntreab\u0103',
		undo			: 'Starea anterioar\u0103',
		redo			: 'Starea ulterioar\u0103(redo)',
		skip			: 'S\u0103ri',
		skipAll			: 'S\u0103ri peste toate',
		makeDecision	: 'Ce ac\u021Biune trebuie luat\u0103?',
		rememberDecision: 'Re\u021Bine ac\u021Biunea pe viitor'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'ro',

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
	DateTime : 'dd/mm/yyyy HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Dosare',
	FolderLoading	: '\xCEnc\u0103rcare...',
	FolderNew		: 'Te rug\u0103m s\u0103 introduci numele dosarului nou: ',
	FolderRename	: 'Te rug\u0103m s\u0103 introduci numele nou al dosarului: ',
	FolderDelete	: 'E\u0219ti sigur c\u0103 vrei s\u0103 \u0219tergi dosarul "%1"?',
	FolderRenaming	: ' (Redenumire...)',
	FolderDeleting	: ' (\u0218tergere...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Te rug\u0103m s\u0103 introduci numele nou al fi\u0219ierului: ',
	FileRenameExt	: 'E\u0219ti sigur c\u0103 vrei s\u0103 schimbi extensia fi\u0219ierului? Fi\u0219ierul poate deveni inutilizabil.',
	FileRenaming	: 'Redenumire...',
	FileDelete		: 'E\u0219ti sigur c\u0103 vrei s\u0103 \u0219tergi fi\u0219ierul "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: '\xCEnc\u0103rcare...',
	FilesEmpty		: 'Dosarul este gol.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Co\u0219',
	BasketClear			: 'Gole\u0219te co\u0219',
	BasketRemove		: 'Elimin\u0103 din co\u0219',
	BasketOpenFolder	: 'Deschide dosarul p\u0103rinte',
	BasketTruncateConfirm : 'Sigur dore\u0219ti s\u0103 elimini toate fi\u0219ierele din co\u0219?',
	BasketRemoveConfirm	: 'Sigur dore\u0219ti s\u0103 elimini fi\u0219ierul "%1" din co\u0219?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Niciun fi\u0219ier \xEEn co\u0219, trage \u0219i a\u0219eaz\u0103 cu mouse-ul.',
	BasketCopyFilesHere	: 'Copiaz\u0103 fi\u0219iere din co\u0219',
	BasketMoveFilesHere	: 'Mut\u0103 fi\u0219iere din co\u0219',

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
	Upload		: '\xCEncarc\u0103',
	UploadTip	: '\xCEncarc\u0103 un fi\u0219ier nou',
	Refresh		: 'Re\xEEmprosp\u0103tare',
	Settings	: 'Set\u0103ri',
	Help		: 'Ajutor',
	HelpTip		: 'Ajutor',

	// Context Menus
	Select			: 'Selecteaz\u0103',
	SelectThumbnail : 'Selecteaz\u0103 Thumbnail',
	View			: 'Vizualizeaz\u0103',
	Download		: 'Descarc\u0103',

	NewSubFolder	: 'Subdosar nou',
	Rename			: 'Redenume\u0219te',
	Delete			: '\u0218terge',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copiaz\u0103 aici',
	MoveDragDrop	: 'Mut\u0103 aici',

	// Dialogs
	RenameDlgTitle		: 'Redenume\u0219te',
	NewNameDlgTitle		: 'Nume nou',
	FileExistsDlgTitle	: 'Fi\u0219ierul exist\u0103 deja',
	SysErrorDlgTitle : 'Eroare de sistem',

	FileOverwrite	: 'Suprascriere',
	FileAutorename	: 'Auto-redenumire',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Anuleaz\u0103',
	CloseBtn	: '\xCEnchide',

	// Upload Panel
	UploadTitle			: '\xCEncarc\u0103 un fi\u0219ier nou',
	UploadSelectLbl		: 'Selecteaz\u0103 un fi\u0219ier de \xEEnc\u0103rcat',
	UploadProgressLbl	: '(\xCEnc\u0103rcare \xEEn progres, te rog a\u0219teapt\u0103...)',
	UploadBtn			: '\xCEncarc\u0103 fi\u0219ierul selectat',
	UploadBtnCancel		: 'Anuleaz\u0103',

	UploadNoFileMsg		: 'Te rug\u0103m s\u0103 selectezi un fi\u0219ier din computer.',
	UploadNoFolder		: 'Te rug\u0103m s\u0103 selectezi un dosar \xEEnainte de a \xEEnc\u0103rca.',
	UploadNoPerms		: '\xCEnc\u0103rcare fi\u0219ier nepermis\u0103.',
	UploadUnknError		: 'Eroare la trimiterea fi\u0219ierului.',
	UploadExtIncorrect	: 'Extensie fi\u0219ier nepermis\u0103 \xEEn acest dosar.',

	// Flash Uploads
	UploadLabel			: 'Fi\u0219iere de \xEEnc\u0103rcat',
	UploadTotalFiles	: 'Total fi\u0219iere:',
	UploadTotalSize		: 'Total m\u0103rime:',
	UploadSend			: '\xCEncarc\u0103',
	UploadAddFiles		: 'Adaug\u0103 fi\u0219iere',
	UploadClearFiles	: 'Renun\u021B\u0103 la toate',
	UploadCancel		: 'Anuleaz\u0103 \xEEnc\u0103rcare',
	UploadRemove		: 'Elimin\u0103',
	UploadRemoveTip		: 'Elimin\u0103 !f',
	UploadUploaded		: '\xCEncarc\u0103 !n%',
	UploadProcessing	: 'Prelucrare...',

	// Settings Panel
	SetTitle		: 'Set\u0103ri',
	SetView			: 'Vizualizeaz\u0103:',
	SetViewThumb	: 'Thumbnails',
	SetViewList		: 'List\u0103',
	SetDisplay		: 'Afi\u0219eaz\u0103:',
	SetDisplayName	: 'Nume fi\u0219ier',
	SetDisplayDate	: 'Dat\u0103',
	SetDisplaySize	: 'M\u0103rime fi\u0219ier',
	SetSort			: 'Sortare:',
	SetSortName		: 'dup\u0103 nume fi\u0219ier',
	SetSortDate		: 'dup\u0103 dat\u0103',
	SetSortSize		: 'dup\u0103 m\u0103rime',
	SetSortExtension		: 'dup\u0103 extensie',

	// Status Bar
	FilesCountEmpty : '<Dosar Gol>',
	FilesCountOne	: '1 fi\u0219ier',
	FilesCountMany	: '%1 fi\u0219iere',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Nu a fost posibil\u0103 finalizarea cererii. (Eroare %1)',
	Errors :
	{
	 10 : 'Comand\u0103 invalid\u0103.',
	 11 : 'Tipul de resurs\u0103 nu a fost specificat \xEEn cerere.',
	 12 : 'Tipul de resurs\u0103 cerut nu este valid.',
	102 : 'Nume fi\u0219ier sau nume dosar invalid.',
	103 : 'Nu a fost posibili\u0103 finalizarea cererii din cauza restric\u021Biilor de autorizare.',
	104 : 'Nu a fost posibili\u0103 finalizarea cererii din cauza restric\u021Biilor de permisiune la sistemul de fi\u0219iere.',
	105 : 'Extensie fi\u0219ier invalid\u0103.',
	109 : 'Cerere invalid\u0103.',
	110 : 'Eroare necunoscut\u0103.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Exist\u0103 deja un fi\u0219ier sau un dosar cu acela\u0219i nume.',
	116 : 'Dosar neg\u0103sit. Te rog \xEEmprosp\u0103teaz\u0103 \u0219i \xEEncearc\u0103 din nou.',
	117 : 'Fi\u0219ier neg\u0103sit. Te rog \xEEmprosp\u0103teaz\u0103 lista de fi\u0219iere \u0219i \xEEncearc\u0103 din nou.',
	118 : 'Calea sursei \u0219i a \u021Bintei sunt egale.',
	201 : 'Un fi\u0219ier cu acela\u0219i nume este deja disponibil. Fi\u0219ierul \xEEnc\u0103rcat a fost redenumit cu "%1".',
	202 : 'Fi\u0219ier invalid.',
	203 : 'Fi\u0219ier invalid. M\u0103rimea fi\u0219ierului este prea mare.',
	204 : 'Fi\u0219ierul \xEEnc\u0103rcat este corupt.',
	205 : 'Niciun dosar temporar nu este disponibil pentru \xEEnc\u0103rcarea pe server.',
	206 : '\xCEnc\u0103rcare anulat\u0103 din motive de securitate. Fi\u0219ierul con\u021Bine date asem\u0103n\u0103toare cu HTML.',
	207 : 'Fi\u0219ierul \xEEnc\u0103rcat a fost redenumit cu "%1".',
	300 : 'Mutare fi\u0219ier(e) e\u0219uat\u0103.',
	301 : 'Copiere fi\u0219ier(e) e\u0219uat\u0103.',
	500 : 'Browser-ul de fi\u0219iere este dezactivat din motive de securitate. Te rog contacteaz\u0103 administratorul de sistem \u0219i verific\u0103 configurarea de fi\u0219iere CKFinder.',
	501 : 'Func\u021Bionalitatea de creat thumbnails este dezactivat\u0103.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Numele fi\u0219ierului nu poate fi gol.',
		FileExists		: 'Fi\u0219ierul %s exist\u0103 deja.',
		FolderEmpty		: 'Numele dosarului nu poate fi gol.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Numele fi\u0219ierului nu poate con\u021Bine niciunul din urm\u0103toarele caractere: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Numele dosarului nu poate con\u021Bine niciunul din urm\u0103toarele caractere: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nu a fost posibil\u0103 deschiderea fi\u0219ierului \xEEntr-o fereastr\u0103 nou\u0103. Te rug\u0103m s\u0103 configurezi browser-ul \u0219i s\u0103 dezactivezi toate popup-urile blocate pentru acest site.',
		XmlError		: 'Nu a fost posibil\u0103 \xEEnc\u0103rcarea \xEEn mod corespunz\u0103tor a r\u0103spunsului XML de pe serverul web.',
		XmlEmpty		: 'Nu a fost posibil\u0103 \xEEnc\u0103rcarea r\u0103spunsului XML de pe serverul web. Serverul a returnat un r\u0103spuns gol.',
		XmlRawResponse	: 'R\u0103spuns brut de la server: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensioneaz\u0103 %s',
		sizeTooBig		: 'Nu se pot seta \xEEn\u0103l\u021Bimea sau l\u0103\u021Bimea unei imagini la o valoare mai mare dec\xE2t dimesiunea original\u0103 (%size).',
		resizeSuccess	: 'Imagine redimensionat\u0103 cu succes.',
		thumbnailNew	: 'Creaz\u0103 un thumbnail nou',
		thumbnailSmall	: 'Mic (%s)',
		thumbnailMedium	: 'Mediu (%s)',
		thumbnailLarge	: 'Mare (%s)',
		newSize			: 'Seteaz\u0103 o dimensiune nou\u0103',
		width			: 'L\u0103\u021Bime',
		height			: '\xCEn\u0103l\u021Bime',
		invalidHeight	: '\xCEn\u0103l\u021Bime invalid\u0103.',
		invalidWidth	: 'L\u0103\u021Bime invalid\u0103.',
		invalidName		: 'Nume fi\u0219ier invalid.',
		newImage		: 'Creeaz\u0103 o imagine nou\u0103',
		noExtensionChange : 'Extensia fi\u0219ierului nu poate fi schimbat\u0103.',
		imageSmall		: 'Imaginea surs\u0103 este prea mic\u0103.',
		contextMenuName	: 'Redimensioneaz\u0103',
		lockRatio		: 'Blocheaz\u0103 raport',
		resetSize		: 'Reseteaz\u0103 dimensiunea'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Salveaz\u0103',
		fileOpenError	: 'Fi\u0219ierul nu a putut fi deschis.',
		fileSaveSuccess	: 'Fi\u0219ier salvat cu succes.',
		contextMenuName	: 'Editeaz\u0103',
		loadingFile		: '\xCEnc\u0103rcare fi\u0219ier, te rog a\u0219teapt\u0103...'
	},

	Maximize :
	{
		maximize : 'Maximizare',
		minimize : 'Minimizare'
	},

	Gallery :
	{
		current : 'Imaginea {current} din {total}'
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
		searchPlaceholder : 'C\u0103utare'
	}
};
