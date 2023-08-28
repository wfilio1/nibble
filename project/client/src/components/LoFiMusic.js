import { Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const LoFiMusic = () => {


    // https://getbootstrap.com/docs/5.0/helpers/ratio/
    return (
        <Container>
            <div className="lofi-ratio ratio-16x9">
                <iframe width="1075" height="600" src="https://www.youtube.com/embed/jfKfPfyJRdk" title="LoFi Girl Youtube" allowFullScreen></iframe>
            </div>
            <h4 className="lofi-music-container" >Music provided by LoFi Girl on Youtube</h4>
            <div className="lofi-music-container">
                Click the title above to redirect to the channel itself if you enjoy their playlist!
            </div>
            <div className="lofi-music-container2">
            <Link to="https://lofigirl.com/use-the-music/#use-music-livestream">Credit link and credit questions here</Link>
            </div>
        </Container>
    );
}

export default LoFiMusic;