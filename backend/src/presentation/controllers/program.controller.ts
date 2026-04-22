import type { Request, Response } from 'express';
import { GetProgramsQueryDto } from '@/application/dtos/program.dto';
import type { GetProgramsUseCase, ViewProgramDetailsUseCase, GetProspectusLinkUseCase } from '@/application/use-cases/program/program.use-cases';

export class ProgramController {
  constructor(
    private readonly getProgramsUseCase: GetProgramsUseCase,
    private readonly viewProgramUseCase: ViewProgramDetailsUseCase,
    private readonly getProspectusUseCase: GetProspectusLinkUseCase
  ) {}

  // GET /api/v1/programs?category=Undergraduate
  getPrograms = async (req: Request, res: Response) => {
    try {
      const { category } = GetProgramsQueryDto.parse(req.query);
      const programs = await this.getProgramsUseCase.execute(category);
      res.status(200).json({ success: true, data: programs });
    } catch (error) {
      res.status(400).json({ success: false, error: 'Invalid category filter' });
    }
  };

  // GET /api/v1/programs/:id
  viewProgram = async (req: Request<{ id: string }>, res: Response) => {
    try {
      const program = await this.viewProgramUseCase.execute(req.params.id);
      res.status(200).json({ success: true, data: program });
    } catch (error: any) {
      res.status(404).json({ success: false, error: error.message });
    }
  };

  // GET /api/v1/programs/:id/prospectus
  downloadProspectus = async (req: Request<{ id: string }>, res: Response) => {
    try {
      const linkData = await this.getProspectusUseCase.execute(req.params.id);
      res.status(200).json({ success: true, data: linkData });
      
      // Note: If you were serving actual files locally instead of URLs, 
      // you would use res.download(filepath) here instead of res.json()
    } catch (error: any) {
      res.status(404).json({ success: false, error: error.message });
    }
  };
}