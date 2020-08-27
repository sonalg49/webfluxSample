package com.myWebfluxProject.sampleApplication.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.myWebfluxProject.sampleApplication.domain.Show;
import com.myWebfluxProject.sampleApplication.respositories.ReactiveShowRepository;
import com.myWebfluxProject.sampleApplication.vo.ShowVo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ShowHandler {
	
	@Autowired
	private final ReactiveShowRepository showRepository;
	
	public ShowHandler(ReactiveShowRepository showRepository) {
		this.showRepository= showRepository;
	}

	public Mono<ServerResponse> getShowList(ServerRequest request){
			Flux<Show> showList = showRepository.findAll();
			Flux<ShowVo> showVoList= showList.map(s -> {
				return new ShowVo(s.getId(), s.getTitle());		
		});
			
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVoList, ShowVo.class);
	}
	
	public Mono<ServerResponse> getShowById(ServerRequest request){
		
		String showId = request.pathVariable("id");
		Mono<ServerResponse> notfound= ServerResponse.notFound().build();
		
		Mono<Show> show = showRepository.findById(showId);
		Mono<ShowVo> showVo = Mono.from(show.map(s->{
				return new ShowVo(s.getId(),s.getTitle());
		}));
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVo, ShowVo.class).switchIfEmpty(notfound);
	}
	
	
	
	public Mono<ServerResponse> addShow(ServerRequest request){
		Mono<ShowVo> showVo = request.bodyToMono(ShowVo.class);
		Mono<Show> show = showVo.map(s -> new Show(s.getId(), s.getTitle())).flatMap(showRepository::save);
		
		showVo = Mono.from(show.map(s -> {
		return new ShowVo(s.getId(), s.getTitle());
		
		}));
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVo, ShowVo.class);
	
	}
	
	public Mono<ServerResponse> addAllShow(ServerRequest request){
		Flux<ShowVo> showVoList = request.bodyToFlux(ShowVo.class);
		Flux<Show> showList = showVoList.map(s -> new Show(s.getId(), s.getTitle())).flatMap(showRepository::insert);
		
		showVoList= showList.map(s -> {
			return new ShowVo(s.getId(), s.getTitle());		
	});
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVoList, ShowVo.class);
		
		
	}
	
	public Mono<ServerResponse> updateShow(ServerRequest request){
		
		Mono<ShowVo> showVo = request.bodyToMono(ShowVo.class);
		Mono<Show> show = showVo.map(s -> new Show(s.getId(), s.getTitle())).flatMap(showRepository::save);
		
		showVo = Mono.from(show.map(s -> {
		return new ShowVo(s.getId(), s.getTitle());
		
		}));
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVo, ShowVo.class);
	}
	 
	public Mono<ServerResponse> deleteShow(ServerRequest request){
		
		String showId = request.pathVariable("id");
		Mono<Show> show = showRepository.findById(showId);
		
		return show.flatMap(s -> showRepository.delete(s)).then(ServerResponse.ok().build(Mono.empty()));
		
	}
	
	public Mono<ServerResponse> getShowBytitle(ServerRequest request){
		String showTitle = request.pathVariable("title");
		
		Flux<Show> showList =showRepository.findByTitle(showTitle);
		Flux<ShowVo> showVoList= showList.map(s -> {
			return new ShowVo(s.getId(), s.getTitle());		
	});
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(showVoList, ShowVo.class);
	}
	
	 
}
