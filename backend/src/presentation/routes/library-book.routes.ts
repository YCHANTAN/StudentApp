import { Router } from 'express';
import { UpdateLibraryBookDto } from '@/application/dtos/library-book.dto';
import { libraryBookController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';

export const libraryBookRouter = Router();

libraryBookRouter.get('/', libraryBookController.getLibraryBooks);
libraryBookRouter.put('/:id', validate(UpdateLibraryBookDto), libraryBookController.updateLibraryBook);
