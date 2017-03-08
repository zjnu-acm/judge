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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Danish
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['da'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, ikke tilg\xE6ngelig</span>',
		confirmCancel	: 'Nogle af indstillingerne er blevet \xE6ndret. Er du sikker p\xE5 at lukke dialogen?',
		ok				: 'OK',
		cancel			: 'Annuller',
		confirmationTitle	: 'Bekr\xE6ftelse',
		messageTitle	: 'Information',
		inputTitle		: 'Sp\xF8rgsm\xE5l',
		undo			: 'Fortryd',
		redo			: 'Annuller fortryd',
		skip			: 'Skip',
		skipAll			: 'Skip alle',
		makeDecision	: 'Hvad skal der foretages?',
		rememberDecision: 'Husk denne indstilling'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'da',

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
	DateTime : 'dd-mm-yyyy HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Mapper',
	FolderLoading	: 'Indl\xE6ser...',
	FolderNew		: 'Skriv navnet p\xE5 den nye mappe: ',
	FolderRename	: 'Skriv det nye navn p\xE5 mappen: ',
	FolderDelete	: 'Er du sikker p\xE5, at du vil slette mappen "%1"?',
	FolderRenaming	: ' (Omd\xF8ber...)',
	FolderDeleting	: ' (Sletter...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Skriv navnet p\xE5 den nye fil: ',
	FileRenameExt	: 'Er du sikker p\xE5, at du vil \xE6ndre filtypen? Filen kan muligvis ikke bruges bagefter.',
	FileRenaming	: '(Omd\xF8ber...)',
	FileDelete		: 'Er du sikker p\xE5, at du vil slette filen "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Indl\xE6ser...',
	FilesEmpty		: 'Tom mappe',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Kurv',
	BasketClear			: 'T\xF8m kurv',
	BasketRemove		: 'Fjern fra kurv',
	BasketOpenFolder	: '\xC5ben overordnet mappe',
	BasketTruncateConfirm : 'Er du sikker p\xE5 at du vil t\xF8mme kurven?',
	BasketRemoveConfirm	: 'Er du sikker p\xE5 at du vil slette filen "%1" fra kurven?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Ingen filer i kurven, brug musen til at tr\xE6kke filer til kurven.',
	BasketCopyFilesHere	: 'Kopier Filer fra kurven',
	BasketMoveFilesHere	: 'Flyt Filer fra kurven',

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
	Upload		: 'Upload',
	UploadTip	: 'Upload ny fil',
	Refresh		: 'Opdat\xE9r',
	Settings	: 'Indstillinger',
	Help		: 'Hj\xE6lp',
	HelpTip		: 'Hj\xE6lp',

	// Context Menus
	Select			: 'V\xE6lg',
	SelectThumbnail : 'V\xE6lg thumbnail',
	View			: 'Vis',
	Download		: 'Download',

	NewSubFolder	: 'Ny undermappe',
	Rename			: 'Omd\xF8b',
	Delete			: 'Slet',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopier hertil',
	MoveDragDrop	: 'Flyt hertil',

	// Dialogs
	RenameDlgTitle		: 'Omd\xF8b',
	NewNameDlgTitle		: 'Nyt navn',
	FileExistsDlgTitle	: 'Filen eksisterer allerede',
	SysErrorDlgTitle : 'System fejl',

	FileOverwrite	: 'Overskriv',
	FileAutorename	: 'Auto-omd\xF8b',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Annull\xE9r',
	CloseBtn	: 'Luk',

	// Upload Panel
	UploadTitle			: 'Upload ny fil',
	UploadSelectLbl		: 'V\xE6lg den fil, som du vil uploade',
	UploadProgressLbl	: '(Uploader, vent venligst...)',
	UploadBtn			: 'Upload filen',
	UploadBtnCancel		: 'Annuller',

	UploadNoFileMsg		: 'V\xE6lg en fil p\xE5 din computer.',
	UploadNoFolder		: 'Venligst v\xE6lg en mappe f\xF8r upload startes.',
	UploadNoPerms		: 'Upload er ikke tilladt.',
	UploadUnknError		: 'Fejl ved upload.',
	UploadExtIncorrect	: 'Denne filtype er ikke tilladt i denne mappe.',

	// Flash Uploads
	UploadLabel			: 'Files to Upload',
	UploadTotalFiles	: 'Total antal filer:',
	UploadTotalSize		: 'Total st\xF8rrelse:',
	UploadSend			: 'Upload',
	UploadAddFiles		: 'Tilf\xF8j filer',
	UploadClearFiles	: 'Nulstil filer',
	UploadCancel		: 'Annuller upload',
	UploadRemove		: 'Fjern',
	UploadRemoveTip		: 'Fjern !f',
	UploadUploaded		: 'Uploadede !n%',
	UploadProcessing	: 'Udf\xF8rer...',

	// Settings Panel
	SetTitle		: 'Indstillinger',
	SetView			: 'Vis:',
	SetViewThumb	: 'Thumbnails',
	SetViewList		: 'Liste',
	SetDisplay		: 'Thumbnails:',
	SetDisplayName	: 'Filnavn',
	SetDisplayDate	: 'Dato',
	SetDisplaySize	: 'St\xF8rrelse',
	SetSort			: 'Sortering:',
	SetSortName		: 'efter filnavn',
	SetSortDate		: 'efter dato',
	SetSortSize		: 'efter st\xF8rrelse',
	SetSortExtension		: 'efter filtype',

	// Status Bar
	FilesCountEmpty : '<tom mappe>',
	FilesCountOne	: '1 fil',
	FilesCountMany	: '%1 filer',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Det var ikke muligt at fuldf\xF8re handlingen. (Fejl: %1)',
	Errors :
	{
	 10 : 'Ugyldig handling.',
	 11 : 'Ressourcetypen blev ikke angivet i anmodningen.',
	 12 : 'Ressourcetypen er ikke gyldig.',
	102 : 'Ugyldig fil eller mappenavn.',
	103 : 'Det var ikke muligt at fuldf\xF8re handlingen p\xE5 grund af en begr\xE6nsning i rettigheder.',
	104 : 'Det var ikke muligt at fuldf\xF8re handlingen p\xE5 grund af en begr\xE6nsning i filsystem rettigheder.',
	105 : 'Ugyldig filtype.',
	109 : 'Ugyldig anmodning.',
	110 : 'Ukendt fejl.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'En fil eller mappe med det samme navn eksisterer allerede.',
	116 : 'Mappen blev ikke fundet. Opdat\xE9r listen eller pr\xF8v igen.',
	117 : 'Filen blev ikke fundet. Opdat\xE9r listen eller pr\xF8v igen.',
	118 : 'Originalplacering og destination er ens.',
	201 : 'En fil med det samme filnavn eksisterer allerede. Den uploadede fil er blevet omd\xF8bt til "%1".',
	202 : 'Ugyldig fil.',
	203 : 'Ugyldig fil. Filst\xF8rrelsen er for stor.',
	204 : 'Den uploadede fil er korrupt.',
	205 : 'Der er ikke en midlertidig mappe til upload til r\xE5dighed p\xE5 serveren.',
	206 : 'Upload annulleret af sikkerhedsm\xE6ssige \xE5rsager. Filen indeholder HTML-lignende data.',
	207 : 'Den uploadede fil er blevet omd\xF8bt til "%1".',
	300 : 'Flytning af fil(er) fejlede.',
	301 : 'Kopiering af fil(er) fejlede.',
	500 : 'Filbrowseren er deaktiveret af sikkerhedsm\xE6ssige \xE5rsager. Kontakt systemadministratoren eller kontroll\xE9r CKFinders konfigurationsfil.',
	501 : 'Underst\xF8ttelse af thumbnails er deaktiveret.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Filnavnet m\xE5 ikke v\xE6re tomt.',
		FileExists		: 'Fil %erne eksisterer allerede.',
		FolderEmpty		: 'Mappenavnet m\xE5 ikke v\xE6re tomt.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Filnavnet m\xE5 ikke indeholde et af f\xF8lgende tegn: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Mappenavnet m\xE5 ikke indeholde et af f\xF8lgende tegn: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Det var ikke muligt at \xE5bne filen i et nyt vindue. Kontroll\xE9r konfigurationen i din browser, og deaktiv\xE9r eventuelle popup-blokkere for denne hjemmeside.',
		XmlError		: 'Det var ikke muligt at hente den korrekte XML kode fra serveren.',
		XmlEmpty		: 'Det var ikke muligt at hente den korrekte XML kode fra serveren. Serveren returnerede et tomt svar.',
		XmlRawResponse	: 'Serveren returenede f\xF8lgende output: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Rediger st\xF8rrelse %s',
		sizeTooBig		: 'Kan ikke \xE6ndre billedets h\xF8jde eller bredde til en v\xE6rdi st\xF8rre end dets originale st\xF8rrelse (%size).',
		resizeSuccess	: 'St\xF8rrelsen er nu \xE6ndret.',
		thumbnailNew	: 'Opret ny thumbnail',
		thumbnailSmall	: 'Lille (%s)',
		thumbnailMedium	: 'Mellem (%s)',
		thumbnailLarge	: 'Stor (%s)',
		newSize			: 'Rediger st\xF8rrelse',
		width			: 'Bredde',
		height			: 'H\xF8jde',
		invalidHeight	: 'Ugyldig h\xF8jde.',
		invalidWidth	: 'Ugyldig bredde.',
		invalidName		: 'Ugyldigt filenavn.',
		newImage		: 'Opret nyt billede.',
		noExtensionChange : 'Filtypen kan ikke \xE6ndres.',
		imageSmall		: 'Originalfilen er for lille.',
		contextMenuName	: 'Rediger st\xF8rrelse',
		lockRatio		: 'L\xE5s st\xF8rrelsesforhold',
		resetSize		: 'Nulstil st\xF8rrelse'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Gem',
		fileOpenError	: 'Filen kan ikke \xE5bnes.',
		fileSaveSuccess	: 'Filen er nu gemt.',
		contextMenuName	: 'Rediger',
		loadingFile		: 'Henter fil, vent venligst...'
	},

	Maximize :
	{
		maximize : 'Maxim\xE9r',
		minimize : 'Minim\xE9r'
	},

	Gallery :
	{
		current : 'Billede {current} ud af {total}'
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
		searchPlaceholder : 'S\xF8g'
	}
};
