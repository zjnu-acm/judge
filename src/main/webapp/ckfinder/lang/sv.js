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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Swedish
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['sv'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, Ej tillg\xE4nglig</span>',
		confirmCancel	: 'N\xE5gra av alternativen har \xE4ndrats. \xC4r du s\xE4ker p\xE5 att du vill st\xE4nga dialogrutan?',
		ok				: 'OK',
		cancel			: 'Avbryt',
		confirmationTitle	: 'Bekr\xE4ftelse',
		messageTitle	: 'Information',
		inputTitle		: 'Fr\xE5ga',
		undo			: '\xC5ngra',
		redo			: 'G\xF6r om',
		skip			: 'Hoppa \xF6ver',
		skipAll			: 'Hoppa \xF6ver alla',
		makeDecision	: 'Vilken \xE5tg\xE4rd ska utf\xF6ras?',
		rememberDecision: 'Kom ih\xE5g mitt val'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'sv',

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
	DateTime : 'yyyy-mm-dd HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Mappar',
	FolderLoading	: 'Laddar...',
	FolderNew		: 'Skriv namnet p\xE5 den nya mappen: ',
	FolderRename	: 'Skriv det nya namnet p\xE5 mappen: ',
	FolderDelete	: '\xC4r du s\xE4ker p\xE5 att du vill radera mappen "%1"?',
	FolderRenaming	: ' (Byter mappens namn...)',
	FolderDeleting	: ' (Raderar...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Skriv det nya filnamnet: ',
	FileRenameExt	: '\xC4r du s\xE4ker p\xE5 att du vill \xE4ndra fil\xE4ndelsen? Filen kan bli oanv\xE4ndbar.',
	FileRenaming	: 'Byter filnamn...',
	FileDelete		: '\xC4r du s\xE4ker p\xE5 att du vill radera filen "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laddar...',
	FilesEmpty		: 'Mappen \xE4r tom.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Filkorg',
	BasketClear			: 'Rensa filkorgen',
	BasketRemove		: 'Ta bort fr\xE5n korgen',
	BasketOpenFolder	: '\xD6ppna \xF6verliggande mapp',
	BasketTruncateConfirm : 'Vill du verkligen ta bort alla filer fr\xE5n korgen?',
	BasketRemoveConfirm	: 'Vill du verkligen ta bort filen "%1" fr\xE5n korgen?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Inga filer i korgen, dra och sl\xE4pp n\xE5gra.',
	BasketCopyFilesHere	: 'Kopiera filer fr\xE5n korgen',
	BasketMoveFilesHere	: 'Flytta filer fr\xE5n korgen',

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
	Upload		: 'Ladda upp',
	UploadTip	: 'Ladda upp en ny fil',
	Refresh		: 'Uppdatera',
	Settings	: 'Inst\xE4llningar',
	Help		: 'Hj\xE4lp',
	HelpTip		: 'Hj\xE4lp',

	// Context Menus
	Select			: 'Infoga bild',
	SelectThumbnail : 'Infoga som tumnagel',
	View			: 'Visa',
	Download		: 'Ladda ner',

	NewSubFolder	: 'Ny Undermapp',
	Rename			: 'Byt namn',
	Delete			: 'Radera',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopiera hit',
	MoveDragDrop	: 'Flytta hit',

	// Dialogs
	RenameDlgTitle		: 'Byt namn',
	NewNameDlgTitle		: 'Nytt namn',
	FileExistsDlgTitle	: 'Filen finns redan',
	SysErrorDlgTitle : 'Systemfel',

	FileOverwrite	: 'Skriv \xF6ver',
	FileAutorename	: 'Auto-namn\xE4ndring',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Avbryt',
	CloseBtn	: 'St\xE4ng',

	// Upload Panel
	UploadTitle			: 'Ladda upp en ny fil',
	UploadSelectLbl		: 'V\xE4lj fil att ladda upp',
	UploadProgressLbl	: '(Laddar upp filen, var god v\xE4nta...)',
	UploadBtn			: 'Ladda upp den valda filen',
	UploadBtnCancel		: 'Avbryt',

	UploadNoFileMsg		: 'V\xE4lj en fil fr\xE5n din dator.',
	UploadNoFolder		: 'V\xE4lj en mapp f\xF6re uppladdning.',
	UploadNoPerms		: 'Filuppladdning ej till\xE5ten.',
	UploadUnknError		: 'Fel vid filuppladdning.',
	UploadExtIncorrect	: 'Fil\xE4ndelsen \xE4r inte till\xE5ten i denna mapp.',

	// Flash Uploads
	UploadLabel			: 'Filer att ladda upp',
	UploadTotalFiles	: 'Totalt antal filer:',
	UploadTotalSize		: 'Total storlek:',
	UploadSend			: 'Ladda upp',
	UploadAddFiles		: 'L\xE4gg till filer',
	UploadClearFiles	: 'Rensa filer',
	UploadCancel		: 'Avbryt uppladdning',
	UploadRemove		: 'Ta bort',
	UploadRemoveTip		: 'Ta bort !f',
	UploadUploaded		: 'Uppladdat !n%',
	UploadProcessing	: 'Bearbetar...',

	// Settings Panel
	SetTitle		: 'Inst\xE4llningar',
	SetView			: 'Visa:',
	SetViewThumb	: 'Tumnaglar',
	SetViewList		: 'Lista',
	SetDisplay		: 'Visa:',
	SetDisplayName	: 'Filnamn',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Storlek',
	SetSort			: 'Sortering:',
	SetSortName		: 'Filnamn',
	SetSortDate		: 'Datum',
	SetSortSize		: 'Storlek',
	SetSortExtension		: 'Fil\xE4ndelse',

	// Status Bar
	FilesCountEmpty : '<Tom Mapp>',
	FilesCountOne	: '1 fil',
	FilesCountMany	: '%1 filer',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Beg\xE4ran kunde inte utf\xF6ras eftersom ett fel uppstod. (Fel %1)',
	Errors :
	{
	 10 : 'Ogiltig beg\xE4ran.',
	 11 : 'Resursens typ var inte specificerad i f\xF6rfr\xE5gan.',
	 12 : 'Den efterfr\xE5gade resurstypen \xE4r inte giltig.',
	102 : 'Ogiltigt fil- eller mappnamn.',
	103 : 'Beg\xE4ran kunde inte utf\xF6ras p.g.a. restriktioner av r\xE4ttigheterna.',
	104 : 'Beg\xE4ran kunde inte utf\xF6ras p.g.a. restriktioner av r\xE4ttigheter i filsystemet.',
	105 : 'Ogiltig fil\xE4ndelse.',
	109 : 'Ogiltig beg\xE4ran.',
	110 : 'Ok\xE4nt fel.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'En fil eller mapp med aktuellt namn finns redan.',
	116 : 'Mappen kunde inte hittas. Var god uppdatera sidan och f\xF6rs\xF6k igen.',
	117 : 'Filen kunde inte hittas. Var god uppdatera sidan och f\xF6rs\xF6k igen.',
	118 : 'S\xF6kv\xE4g till k\xE4lla och m\xE5l \xE4r identisk.',
	201 : 'En fil med aktuellt namn fanns redan. Den uppladdade filen har d\xF6pts om till "%1".',
	202 : 'Ogiltig fil.',
	203 : 'Ogiltig fil. Filen var f\xF6r stor.',
	204 : 'Den uppladdade filen var korrupt.',
	205 : 'En tillf\xE4llig mapp f\xF6r uppladdning \xE4r inte tillg\xE4nglig p\xE5 servern.',
	206 : 'Uppladdningen stoppades av s\xE4kerhetssk\xE4l. Filen inneh\xE5ller HTML-liknande data.',
	207 : 'Den uppladdade filen har d\xF6pts om till "%1".',
	300 : 'Flytt av fil(er) misslyckades.',
	301 : 'Kopiering av fil(er) misslyckades.',
	500 : 'Filhanteraren har stoppats av s\xE4kerhetssk\xE4l. Var god kontakta administrat\xF6ren f\xF6r att kontrollera konfigurationsfilen f\xF6r CKFinder.',
	501 : 'St\xF6d f\xF6r tumnaglar har st\xE4ngts av.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Filnamnet f\xE5r inte vara tomt.',
		FileExists		: 'Filen %s finns redan.',
		FolderEmpty		: 'Mappens namn f\xE5r inte vara tomt.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Filnamnet f\xE5r inte inneh\xE5lla n\xE5got av f\xF6ljande tecken: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Mappens namn f\xE5r inte inneh\xE5lla n\xE5got av f\xF6ljande tecken: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Det gick inte att \xF6ppna filen i ett nytt f\xF6nster. \xC4ndra inst\xE4llningarna i din webbl\xE4sare s\xE5 att den till\xE5ter popup-f\xF6nster p\xE5 den h\xE4r webbplatsen.',
		XmlError		: 'Det gick inte att ladda XML-svaret fr\xE5n webbservern ordentligt.',
		XmlEmpty		: 'Det gick inte att ladda XML-svaret fr\xE5n webbservern. Servern returnerade ett tomt svar.',
		XmlRawResponse	: 'Svar fr\xE5n servern: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Storleks\xE4ndra %s',
		sizeTooBig		: 'Bildens h\xF6jd eller bredd kan inte vara st\xF6rre \xE4n originalfilens storlek (%size).',
		resizeSuccess	: 'Storleks\xE4ndring lyckades.',
		thumbnailNew	: 'Skapa en ny tumnagel',
		thumbnailSmall	: 'Liten (%s)',
		thumbnailMedium	: 'Mellan (%s)',
		thumbnailLarge	: 'Stor (%s)',
		newSize			: 'V\xE4lj en ny storlek',
		width			: 'Bredd',
		height			: 'H\xF6jd',
		invalidHeight	: 'Ogiltig h\xF6jd.',
		invalidWidth	: 'Ogiltig bredd.',
		invalidName		: 'Ogiltigt filnamn.',
		newImage		: 'Skapa en ny bild',
		noExtensionChange : 'Fil\xE4ndelsen kan inte \xE4ndras.',
		imageSmall		: 'Originalbilden \xE4r f\xF6r liten.',
		contextMenuName	: '\xC4ndra storlek',
		lockRatio		: 'L\xE5s h\xF6jd/bredd f\xF6rh\xE5llanden',
		resetSize		: '\xC5terst\xE4ll storlek'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Spara',
		fileOpenError	: 'Kan inte \xF6ppna filen.',
		fileSaveSuccess	: 'Filen sparades.',
		contextMenuName	: 'Redigera',
		loadingFile		: 'Laddar fil, var god v\xE4nta...'
	},

	Maximize :
	{
		maximize : 'Maximera',
		minimize : 'Minimera'
	},

	Gallery :
	{
		current : 'Bild {current} av {total}'
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
		searchPlaceholder : 'S\xF6k'
	}
};
