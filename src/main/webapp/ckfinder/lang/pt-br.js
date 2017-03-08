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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Brazilian Portuguese
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['pt-br'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, indispon\xEDvel</span>',
		confirmCancel	: 'Algumas op\xE7\xF5es foram modificadas. Deseja fechar a janela realmente?',
		ok				: 'OK',
		cancel			: 'Cancelar',
		confirmationTitle	: 'Confirma\xE7\xE3o',
		messageTitle	: 'Informa\xE7\xE3o',
		inputTitle		: 'Pergunta',
		undo			: 'Desfazer',
		redo			: 'Refazer',
		skip			: 'Ignorar',
		skipAll			: 'Ignorar todos',
		makeDecision	: 'Que a\xE7\xE3o deve ser tomada?',
		rememberDecision: 'Lembra minha decis\xE3o'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'pt-br',

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
	FoldersTitle	: 'Pastas',
	FolderLoading	: 'Carregando...',
	FolderNew		: 'Favor informar o nome da nova pasta: ',
	FolderRename	: 'Favor informar o nome da nova pasta: ',
	FolderDelete	: 'Voc\xEA tem certeza que deseja apagar a pasta "%1"?',
	FolderRenaming	: ' (Renomeando...)',
	FolderDeleting	: ' (Apagando...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Favor informar o nome do novo arquivo: ',
	FileRenameExt	: 'Voc\xEA tem certeza que deseja alterar a extens\xE3o do arquivo? O arquivo pode ser danificado.',
	FileRenaming	: 'Renomeando...',
	FileDelete		: 'Voc\xEA tem certeza que deseja apagar o arquivo "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Carregando...',
	FilesEmpty		: 'Pasta vazia',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Cesta',
	BasketClear			: 'Limpa Cesta',
	BasketRemove		: 'Remove da cesta',
	BasketOpenFolder	: 'Abre a pasta original',
	BasketTruncateConfirm : 'Remover todos os arquivas da cesta?',
	BasketRemoveConfirm	: 'Remover o arquivo "%1" da cesta?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Nenhum arquivo na cesta, arraste alguns antes.',
	BasketCopyFilesHere	: 'Copia Arquivos da Cesta',
	BasketMoveFilesHere	: 'Move os Arquivos da Cesta',

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
	Upload		: 'Enviar arquivo',
	UploadTip	: 'Enviar novo arquivo',
	Refresh		: 'Atualizar',
	Settings	: 'Configura\xE7\xF5es',
	Help		: 'Ajuda',
	HelpTip		: 'Ajuda',

	// Context Menus
	Select			: 'Selecionar',
	SelectThumbnail : 'Selecionar miniatura',
	View			: 'Visualizar',
	Download		: 'Download',

	NewSubFolder	: 'Nova sub-pasta',
	Rename			: 'Renomear',
	Delete			: 'Apagar',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copia aqui',
	MoveDragDrop	: 'Move aqui',

	// Dialogs
	RenameDlgTitle		: 'Renomeia',
	NewNameDlgTitle		: 'Novo nome',
	FileExistsDlgTitle	: 'O arquivo j\xE1 existe',
	SysErrorDlgTitle : 'Erro de Sistema',

	FileOverwrite	: 'Sobrescrever',
	FileAutorename	: 'Renomeia automaticamente',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Cancelar',
	CloseBtn	: 'Fechar',

	// Upload Panel
	UploadTitle			: 'Enviar novo arquivo',
	UploadSelectLbl		: 'Selecione o arquivo para enviar',
	UploadProgressLbl	: '(Enviado arquivo, favor aguardar...)',
	UploadBtn			: 'Enviar arquivo selecionado',
	UploadBtnCancel		: 'Cancelar',

	UploadNoFileMsg		: 'Favor selecionar o arquivo no seu computador.',
	UploadNoFolder		: 'Favor selecionar a pasta antes the enviar o arquivo.',
	UploadNoPerms		: 'N\xE3o \xE9 permitido o envio de arquivos.',
	UploadUnknError		: 'Erro no envio do arquivo.',
	UploadExtIncorrect	: 'A extens\xE3o deste arquivo n\xE3o \xE9 permitida nesat pasta.',

	// Flash Uploads
	UploadLabel			: 'Arquivos para Enviar',
	UploadTotalFiles	: 'Arquivos:',
	UploadTotalSize		: 'Tamanho:',
	UploadSend			: 'Enviar arquivo',
	UploadAddFiles		: 'Adicionar Arquivos',
	UploadClearFiles	: 'Remover Arquivos',
	UploadCancel		: 'Cancelar Envio',
	UploadRemove		: 'Remover',
	UploadRemoveTip		: 'Remover !f',
	UploadUploaded		: '!n% enviado',
	UploadProcessing	: 'Processando...',

	// Settings Panel
	SetTitle		: 'Configura\xE7\xF5es',
	SetView			: 'Visualizar:',
	SetViewThumb	: 'Miniaturas',
	SetViewList		: 'Lista',
	SetDisplay		: 'Exibir:',
	SetDisplayName	: 'Arquivo',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Tamanho',
	SetSort			: 'Ordenar:',
	SetSortName		: 'por Nome do arquivo',
	SetSortDate		: 'por Data',
	SetSortSize		: 'por Tamanho',
	SetSortExtension		: 'por Extens\xE3o',

	// Status Bar
	FilesCountEmpty : '<Pasta vazia>',
	FilesCountOne	: '1 arquivo',
	FilesCountMany	: '%1 arquivos',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'N\xE3o foi poss\xEDvel completer o seu pedido. (Erro %1)',
	Errors :
	{
	 10 : 'Comando inv\xE1lido.',
	 11 : 'O tipo de recurso n\xE3o foi especificado na solicita\xE7\xE3o.',
	 12 : 'O recurso solicitado n\xE3o \xE9 v\xE1lido.',
	102 : 'Nome do arquivo ou pasta inv\xE1lido.',
	103 : 'N\xE3o foi poss\xEDvel completar a solicita\xE7\xE3o por restri\xE7\xF5es de acesso.',
	104 : 'N\xE3o foi poss\xEDvel completar a solicita\xE7\xE3o por restri\xE7\xF5es de acesso do sistema de arquivos.',
	105 : 'Extens\xE3o de arquivo inv\xE1lida.',
	109 : 'Solicita\xE7\xE3o inv\xE1lida.',
	110 : 'Erro desconhecido.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Uma arquivo ou pasta j\xE1 existe com esse nome.',
	116 : 'Pasta n\xE3o encontrada. Atualize e tente novamente.',
	117 : 'Arquivo n\xE3o encontrado. Atualize a lista de arquivos e tente novamente.',
	118 : 'Origem e destino s\xE3o iguais.',
	201 : 'Um arquivo com o mesmo nome j\xE1 est\xE1 dispon\xEDvel. O arquivo enviado foi renomeado para "%1".',
	202 : 'Arquivo inv\xE1lido.',
	203 : 'Arquivo inv\xE1lido. O tamanho \xE9 muito grande.',
	204 : 'O arquivo enviado est\xE1 corrompido.',
	205 : 'Nenhuma pasta tempor\xE1ria para envio est\xE1 dispon\xEDvel no servidor.',
	206 : 'Transmiss\xE3o cancelada por raz\xF5es de seguran\xE7a. O arquivo contem dados HTML.',
	207 : 'O arquivo enviado foi renomeado para "%1".',
	300 : 'N\xE3o foi poss\xEDvel mover o(s) arquivo(s).',
	301 : 'N\xE3o foi poss\xEDvel copiar o(s) arquivos(s).',
	500 : 'A navega\xE7\xE3o de arquivos est\xE1 desativada por raz\xF5es de seguran\xE7a. Contacte o administrador do sistema.',
	501 : 'O suporte a miniaturas est\xE1 desabilitado.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'O nome do arquivo n\xE3o pode ser vazio.',
		FileExists		: 'O nome %s j\xE1 \xE9 em uso.',
		FolderEmpty		: 'O nome da pasta n\xE3o pode ser vazio.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'O nome do arquivo n\xE3o pode conter nenhum desses caracteres: \n\\ / : * ? " < > |',
		FolderInvChar	: 'O nome da pasta n\xE3o pode conter nenhum desses caracteres: \n\\ / : * ? " < > |',

		PopupBlockView	: 'N\xE3o foi poss\xEDvel abrir o arquivo em outra janela. Configure seu navegador e desabilite o bloqueio a popups para esse site.',
		XmlError		: 'N\xE3o foi poss\xEDvel carregar a resposta XML enviada pelo servidor.',
		XmlEmpty		: 'N\xE3o foi poss\xEDvel carregar a resposta XML enviada pelo servidor. Resposta vazia..',
		XmlRawResponse	: 'Resposta original enviada pelo servidor: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensionar %s',
		sizeTooBig		: 'N\xE3o poss\xEDvel usar dimens\xF5es maiores do que as originais (%size).',
		resizeSuccess	: 'Imagem redimensionada corretamente.',
		thumbnailNew	: 'Cria nova anteprima',
		thumbnailSmall	: 'Pequeno (%s)',
		thumbnailMedium	: 'M\xE9dio (%s)',
		thumbnailLarge	: 'Grande (%s)',
		newSize			: 'Novas dimens\xF5es',
		width			: 'Largura',
		height			: 'Altura',
		invalidHeight	: 'Altura incorreta.',
		invalidWidth	: 'Largura incorreta.',
		invalidName		: 'O nome do arquivo n\xE3o \xE9 v\xE1lido.',
		newImage		: 'Cria nova imagem',
		noExtensionChange : 'A extens\xE3o do arquivo n\xE3o pode ser modificada.',
		imageSmall		: 'A imagem original \xE9 muito pequena.',
		contextMenuName	: 'Redimensionar',
		lockRatio		: 'Travar Propor\xE7\xF5es',
		resetSize		: 'Redefinir para o Tamanho Original'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Salva',
		fileOpenError	: 'N\xE3o \xE9 poss\xEDvel abrir o arquivo.',
		fileSaveSuccess	: 'Arquivo salvado corretamente.',
		contextMenuName	: 'Modificar',
		loadingFile		: 'Carregando arquivo. Por favor aguarde...'
	},

	Maximize :
	{
		maximize : 'Maximizar',
		minimize : 'Minimizar'
	},

	Gallery :
	{
		current : 'Imagem {current} de {total}'
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
		searchPlaceholder : 'Pesquisar'
	}
};
